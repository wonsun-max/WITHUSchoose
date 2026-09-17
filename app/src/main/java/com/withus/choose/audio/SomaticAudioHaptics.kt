package com.withus.choose.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Procedural Audio & Haptics Engine.
 * Synthesizes 108Hz carrier + 4Hz theta wave binaural tones via AudioTrack.
 * Triggers arterial "lub-dub" heartbeat haptic pulses and flash cues.
 */
class SomaticAudioHaptics(private val context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private var binauralTrack: AudioTrack? = null
    private var binauralJob: Job? = null
    private val audioScope = CoroutineScope(Dispatchers.Default)

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
        if (_isMuted.value) {
            stopBinauralTone()
        }
    }

    fun setMuted(muted: Boolean) {
        _isMuted.value = muted
        if (muted) {
            stopBinauralTone()
        }
    }

    /**
     * Synthesizes 108Hz / 112Hz binaural theta tone directly into PCM stereo stream.
     */
    fun startBinauralTone() {
        if (_isMuted.value || binauralJob?.isActive == true) return

        binauralJob = audioScope.launch {
            val sampleRate = 44100
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(sampleRate / 4)

            val track = AudioTrack(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build(),
                bufferSize * 2,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )

            binauralTrack = track
            track.play()

            val leftFreq = 108.0 // Deep grounding carrier
            val rightFreq = 112.0 // 4 Hz differential = Theta state
            val buffer = ShortArray(bufferSize)
            var sampleIndex = 0L

            try {
                while (isActive && !_isMuted.value) {
                    for (i in 0 until bufferSize step 2) {
                        val t = sampleIndex.toDouble() / sampleRate
                        // Soft volume (30%) with gentle envelope
                        val leftSample = (sin(2.0 * PI * leftFreq * t) * 0.25 * Short.MAX_VALUE).toInt()
                        val rightSample = (sin(2.0 * PI * rightFreq * t) * 0.25 * Short.MAX_VALUE).toInt()

                        buffer[i] = leftSample.toShort()
                        buffer[i + 1] = rightSample.toShort()
                        sampleIndex++
                    }
                    track.write(buffer, 0, bufferSize)
                }
            } catch (e: Exception) {
                // Ignore audio write cancellation
            } finally {
                try {
                    track.stop()
                    track.release()
                } catch (e: Exception) {
                    // Ignore release
                }
            }
        }
    }

    fun stopBinauralTone() {
        binauralJob?.cancel()
        binauralJob = null
        try {
            binauralTrack?.stop()
            binauralTrack?.release()
        } catch (e: Exception) {
            // Ignore
        }
        binauralTrack = null
    }

    /**
     * Plays an acute high-resonance chime when an option flashes for 0.5s.
     */
    fun playFlashChime() {
        if (_isMuted.value) return

        audioScope.launch {
            val sampleRate = 44100
            val durationMs = 500
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(numSamples)

            val baseFreq = 660.0 // Crystalline focus frequency
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val decay = exp(-6.0 * (i.toDouble() / numSamples)) // Exponential decay
                val harmonic = sin(2.0 * PI * baseFreq * t) + 0.3 * sin(2.0 * PI * baseFreq * 2.0 * t)
                val sample = (harmonic * decay * 0.5 * Short.MAX_VALUE).toInt()
                buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            try {
                val track = AudioTrack(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build(),
                    AudioFormat.Builder()
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .build(),
                    numSamples * 2,
                    AudioTrack.MODE_STATIC,
                    AudioManager.AUDIO_SESSION_ID_GENERATE
                )
                track.write(buffer, 0, numSamples)
                track.play()
                // Auto cleanup after chime
                kotlinx.coroutines.delay(600)
                track.stop()
                track.release()
            } catch (e: Exception) {
                // Ignore audio chime errors
            }
        }
    }

    /**
     * Subtle "lub-dub" double pulse for live heartbeat synchronization.
     */
    fun triggerHeartbeatHaptic() {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // First beat (lub) + short pause + second beat (dub)
                val timings = longArrayOf(0, 24, 70, 32)
                val amplitudes = intArrayOf(0, 70, 0, 95)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(40)
            }
        } catch (e: Exception) {
            // Ignore haptic vibration errors
        }
    }

    /**
     * Crisp sharp tactile click on the instant of the visual flash.
     */
    fun triggerFlashHaptic() {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(30)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun release() {
        stopBinauralTone()
    }
}
