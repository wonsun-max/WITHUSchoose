package com.withus.choose.sensor

import android.content.Context
import android.os.Build
import android.os.SystemClock
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import java.util.concurrent.Executors
import kotlin.math.PI
import kotlin.math.sin

/**
 * Photoplethysmography (PPG) Biometric Engine.
 * Measures arterial pulse wave via camera flash illumination and fingertip capillary absorption.
 * Features dual-mode support with physiological cardiac synthesis on emulators or sensor unavailability.
 */
class PulseSensorManager(private val context: Context) {

    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    // Real-time telemetry streams
    private val _bpm = MutableStateFlow(72)
    val bpm: StateFlow<Int> = _bpm.asStateFlow()

    private val _ppgWaveformPoint = MutableSharedFlow<Float>(replay = 1, extraBufferCapacity = 64)
    val ppgWaveformPoint: SharedFlow<Float> = _ppgWaveformPoint.asSharedFlow()

    private val _isFingerDetected = MutableStateFlow(false)
    val isFingerDetected: StateFlow<Boolean> = _isFingerDetected.asStateFlow()

    private val _isSimulated = MutableStateFlow(false)
    val isSimulated: StateFlow<Boolean> = _isSimulated.asStateFlow()

    private val _heartbeatEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 4)
    val heartbeatEvent: SharedFlow<Unit> = _heartbeatEvent.asSharedFlow()

    // Tremor / Jitter tracker
    private val _microTremorScore = MutableStateFlow(0.0f)
    val microTremorScore: StateFlow<Float> = _microTremorScore.asStateFlow()

    private var simulationJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    // Signal processing state for PPG
    private val bufferWindow = ArrayDeque<Double>(30)
    private var runningAvg = 0.0
    private var lastPeakTime = 0L
    private val peakIntervals = ArrayDeque<Long>(8)
    private var frameCount = 0

    // Touch coordinate tracking for tremor
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var tremorAccumulator = 0f

    val isEmulator: Boolean by lazy {
        Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                Build.PRODUCT == "google_sdk" ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu")
    }

    /**
     * Start pulse detection. On real hardware with camera permission, initializes CameraX with torch.
     * On emulators or if camera fails, starts automatic physiological cardiac simulation.
     */
    fun startSensing(lifecycleOwner: LifecycleOwner, useSimulationFallback: Boolean = true) {
        if (isEmulator && useSimulationFallback) {
            startSimulation()
            return
        }

        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    cameraProvider = cameraProviderFuture.get()
                    bindCameraAnalysis(lifecycleOwner)
                } catch (e: Exception) {
                    if (useSimulationFallback) startSimulation()
                }
            }, ContextCompat.getMainExecutor(context))
        } catch (e: Exception) {
            if (useSimulationFallback) startSimulation()
        }
    }

    private fun bindCameraAnalysis(lifecycleOwner: LifecycleOwner) {
        val provider = cameraProvider ?: return
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            processFrame(imageProxy)
        }

        try {
            provider.unbindAll()
            camera = provider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis)
            // Enable torch to illuminate the capillary bed
            camera?.cameraControl?.enableTorch(true)
            _isSimulated.value = false
        } catch (e: Exception) {
            // Camera unavailable or permission denied -> graceful fallback to simulation
            startSimulation()
        }
    }

    private fun processFrame(imageProxy: ImageProxy) {
        try {
            val planes = imageProxy.planes
            if (planes.isEmpty()) {
                imageProxy.close()
                return
            }

            // Luminance plane (Y)
            val buffer = planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            var sum = 0L
            val step = 4 // Subsample for performance
            var count = 0
            var i = 0
            while (i < bytes.size) {
                sum += (bytes[i].toInt() and 0xFF)
                count++
                i += step
            }

            val avgLuminance = if (count > 0) sum.toDouble() / count else 0.0

            // Finger check: When fingertip covers the camera + flash, luminance is high and stable
            val fingerCovered = avgLuminance > 50.0
            _isFingerDetected.value = fingerCovered

            if (fingerCovered) {
                frameCount++
                // Running rolling average
                if (bufferWindow.size >= 30) {
                    bufferWindow.removeFirst()
                }
                bufferWindow.addLast(avgLuminance)
                runningAvg = bufferWindow.average()

                // AC Component (pulse wave oscillation)
                val acSignal = avgLuminance - runningAvg
                val normalizedWave = ((acSignal / 15.0).coerceIn(-1.0, 1.0) * 0.5 + 0.5).toFloat()
                _ppgWaveformPoint.tryEmit(normalizedWave)

                // Systolic peak detection
                val now = SystemClock.elapsedRealtime()
                if (acSignal > 1.8 && (now - lastPeakTime) > 360) { // Max ~166 BPM
                    if (lastPeakTime > 0) {
                        val intervalMs = now - lastPeakTime
                        if (peakIntervals.size >= 8) peakIntervals.removeFirst()
                        peakIntervals.addLast(intervalMs)

                        val avgInterval = peakIntervals.average()
                        if (avgInterval > 0) {
                            val computedBpm = (60_000.0 / avgInterval).toInt().coerceIn(48, 160)
                            _bpm.value = computedBpm
                            _heartbeatEvent.tryEmit(Unit)
                        }
                    }
                    lastPeakTime = now
                }
            } else {
                // If finger is off during test on physical device, keep a smooth resting estimate
                _ppgWaveformPoint.tryEmit(0.5f)
            }
        } catch (e: Exception) {
            // Safeguard against frame drops
        } finally {
            imageProxy.close()
        }
    }

    /**
     * Touch tremor measurement: Call on touch motion events during the calibration or flash test.
     */
    fun recordTouchEvent(x: Float, y: Float, pressure: Float) {
        if (lastTouchX > 0f && lastTouchY > 0f) {
            val dx = kotlin.math.abs(x - lastTouchX)
            val dy = kotlin.math.abs(y - lastTouchY)
            val jitter = kotlin.math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            tremorAccumulator = tremorAccumulator * 0.85f + jitter * 0.15f
            _microTremorScore.value = tremorAccumulator.coerceIn(0f, 10f)
        }
        lastTouchX = x
        lastTouchY = y
    }

    /**
     * Synthesizes authentic autonomic cardiovascular rhythm when running in emulator.
     */
    private fun startSimulation() {
        if (simulationJob?.isActive == true) return
        _isSimulated.value = true
        _isFingerDetected.value = true

        simulationJob = coroutineScope.launch {
            var phase = 0.0
            var simulatedBpm = 72
            var lastBeatTime = SystemClock.elapsedRealtime()

            while (isActive) {
                // Breathing/RSA (Respiratory Sinus Arrhythmia) modulation
                val rsa = sin(phase * 0.1) * 3.0
                val currentTargetBpm = (simulatedBpm + rsa).toInt()
                _bpm.value = currentTargetBpm

                // Arterial dicrotic wave simulation: systolic peak + dicrotic notch
                val systolic = sin(phase)
                val dicrotic = sin(phase * 2.0) * 0.3
                val rawWave = (systolic + dicrotic).coerceIn(-1.0, 1.0)
                val normalizedWave = ((rawWave * 0.5) + 0.5).toFloat()
                _ppgWaveformPoint.emit(normalizedWave)

                // Heartbeat trigger at wave peak
                val now = SystemClock.elapsedRealtime()
                val beatInterval = (60_000 / currentTargetBpm).toLong()
                if (now - lastBeatTime >= beatInterval) {
                    _heartbeatEvent.tryEmit(Unit)
                    lastBeatTime = now
                }

                phase += (2.0 * PI * (currentTargetBpm / 60.0)) / 30.0 // 30 FPS sampling
                delay(33) // ~30 FPS
            }
        }
    }

    /**
     * Simulates autonomic shift for testing (e.g. sympathetic bump or parasympathetic relief).
     */
    fun simulateAutonomicShift(deltaBpm: Int, tremorDelta: Float) {
        if (_isSimulated.value) {
            _bpm.value = (_bpm.value + deltaBpm).coerceIn(52, 140)
            _microTremorScore.value = (_microTremorScore.value + tremorDelta).coerceIn(0.2f, 9.8f)
        }
    }

    fun stopSensing() {
        simulationJob?.cancel()
        simulationJob = null
        try {
            camera?.cameraControl?.enableTorch(false)
            cameraProvider?.unbindAll()
        } catch (e: Exception) {
            // Ignore teardown issues
        }
    }
}
