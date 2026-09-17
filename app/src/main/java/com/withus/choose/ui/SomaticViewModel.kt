package com.withus.choose.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.withus.choose.audio.SomaticAudioHaptics
import com.withus.choose.data.local.SomaticDatabase
import com.withus.choose.data.model.VerdictEntity
import com.withus.choose.data.repository.VerdictRepository
import com.withus.choose.sensor.PulseSensorManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class SomaticViewModel(application: Application) : AndroidViewModel(application) {

    val sensorManager = PulseSensorManager(application)
    val audioHaptics = SomaticAudioHaptics(application)
    private val repository: VerdictRepository

    val historyList: StateFlow<List<VerdictEntity>>

    // Navigation state
    private val _currentScreen = MutableStateFlow(SomaticScreen.DILEMMA)
    val currentScreen: StateFlow<SomaticScreen> = _currentScreen.asStateFlow()

    // Screen 1: Dilemma Inputs
    private val _optionA = MutableStateFlow("Accept the corporate job")
    val optionA: StateFlow<String> = _optionA.asStateFlow()

    private val _optionB = MutableStateFlow("Start my own studio")
    val optionB: StateFlow<String> = _optionB.asStateFlow()

    // Screen 2: Calibration state
    private val _calibrationProgress = MutableStateFlow(0f)
    val calibrationProgress: StateFlow<Float> = _calibrationProgress.asStateFlow()

    private val _calibrationSecondsRemaining = MutableStateFlow(10)
    val calibrationSecondsRemaining: StateFlow<Int> = _calibrationSecondsRemaining.asStateFlow()

    private val _baselineBpm = MutableStateFlow(72)
    val baselineBpm: StateFlow<Int> = _baselineBpm.asStateFlow()

    // Screen 3: Flash test state
    private val _flashPhase = MutableStateFlow(FlashTestPhase.IDLE)
    val flashPhase: StateFlow<FlashTestPhase> = _flashPhase.asStateFlow()

    private val _countdownNumber = MutableStateFlow(3)
    val countdownNumber: StateFlow<Int> = _countdownNumber.asStateFlow()

    private val _activeFlashText = MutableStateFlow("")
    val activeFlashText: StateFlow<String> = _activeFlashText.asStateFlow()

    private val _resetSecondsRemaining = MutableStateFlow(5)
    val resetSecondsRemaining: StateFlow<Int> = _resetSecondsRemaining.asStateFlow()

    // Screen 4: Verdict state
    private val _currentVerdict = MutableStateFlow<SomaticVerdictData?>(null)
    val currentVerdict: StateFlow<SomaticVerdictData?> = _currentVerdict.asStateFlow()

    private var activeTestJob: Job? = null
    private var heartbeatObserverJob: Job? = null

    init {
        val db = SomaticDatabase.getDatabase(application)
        repository = VerdictRepository(db.verdictDao())
        historyList = repository.allVerdicts.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

        // Subtle haptic sync on detected heartbeat
        heartbeatObserverJob = viewModelScope.launch {
            sensorManager.heartbeatEvent.collect {
                if (_currentScreen.value == SomaticScreen.CALIBRATION || _currentScreen.value == SomaticScreen.FLASH_TEST) {
                    audioHaptics.triggerHeartbeatHaptic()
                }
            }
        }
    }

    fun setOptionA(text: String) { _optionA.value = text }
    fun setOptionB(text: String) { _optionB.value = text }

    fun swapOptions() {
        val temp = _optionA.value
        _optionA.value = _optionB.value
        _optionB.value = temp
        audioHaptics.triggerFlashHaptic()
    }

    fun applyPreset(presetA: String, presetB: String) {
        _optionA.value = presetA
        _optionB.value = presetB
    }

    fun startCalibration() {
        if (_optionA.value.isBlank() || _optionB.value.isBlank()) return
        _currentScreen.value = SomaticScreen.CALIBRATION
        _calibrationProgress.value = 0f
        _calibrationSecondsRemaining.value = 10

        activeTestJob?.cancel()
        activeTestJob = viewModelScope.launch {
            val collectedBpms = mutableListOf<Int>()
            for (second in 1..10) {
                delay(1000)
                _calibrationSecondsRemaining.value = 10 - second
                _calibrationProgress.value = second / 10f
                collectedBpms.add(sensorManager.bpm.value)
            }

            val validBpms = collectedBpms.filter { it in 48..150 }
            val medianBpm = if (validBpms.isNotEmpty()) {
                validBpms.sorted()[validBpms.size / 2]
            } else {
                72
            }
            _baselineBpm.value = medianBpm

            // Smooth transition to Flash Moment
            delay(400)
            startFlashTest()
        }
    }

    private fun startFlashTest() {
        _currentScreen.value = SomaticScreen.FLASH_TEST
        _flashPhase.value = FlashTestPhase.IDLE

        // Calming ambient theta tone
        audioHaptics.startBinauralTone()

        activeTestJob?.cancel()
        activeTestJob = viewModelScope.launch {
            // 1. Gentle visual countdown: 3... 2... 1...
            _flashPhase.value = FlashTestPhase.COUNTDOWN
            for (n in 3 downTo 1) {
                _countdownNumber.value = n
                delay(1000)
            }

            // 2. Unpredictable Silence A (800ms - 1300ms) to allow raw reflex
            _flashPhase.value = FlashTestPhase.UNPREDICTABLE_SILENCE_A
            val randomSilenceA = Random.nextLong(800, 1300)
            delay(randomSilenceA)

            // 3. 0.5s Flash Option A
            _flashPhase.value = FlashTestPhase.FLASH_A
            _activeFlashText.value = _optionA.value
            audioHaptics.playFlashChime()
            audioHaptics.triggerFlashHaptic()
            delay(500)

            // 4. Impulse Window A (Pitch black, 3.0s recording)
            _flashPhase.value = FlashTestPhase.IMPULSE_A
            _activeFlashText.value = ""
            val startBpmA = sensorManager.bpm.value
            val initialTremorA = sensorManager.microTremorScore.value

            if (sensorManager.isSimulated.value) {
                sensorManager.simulateAutonomicShift(deltaBpm = 7, tremorDelta = 1.2f)
            }
            delay(3000)
            val endBpmA = sensorManager.bpm.value
            val endTremorA = sensorManager.microTremorScore.value

            // 5. Breathing Reset Pause (5 seconds)
            _flashPhase.value = FlashTestPhase.RESET_PAUSE
            for (sec in 5 downTo 1) {
                _resetSecondsRemaining.value = sec
                delay(1000)
            }

            // 6. Unpredictable Silence B (800ms - 1300ms)
            _flashPhase.value = FlashTestPhase.UNPREDICTABLE_SILENCE_B
            val randomSilenceB = Random.nextLong(800, 1300)
            delay(randomSilenceB)

            // 7. 0.5s Flash Option B
            _flashPhase.value = FlashTestPhase.FLASH_B
            _activeFlashText.value = _optionB.value
            audioHaptics.playFlashChime()
            audioHaptics.triggerFlashHaptic()
            delay(500)

            // 8. Impulse Window B (Pitch black, 3.0s recording)
            _flashPhase.value = FlashTestPhase.IMPULSE_B
            _activeFlashText.value = ""
            val startBpmB = sensorManager.bpm.value
            val initialTremorB = sensorManager.microTremorScore.value

            if (sensorManager.isSimulated.value) {
                sensorManager.simulateAutonomicShift(deltaBpm = -5, tremorDelta = -1.0f)
            }
            delay(3000)
            val endBpmB = sensorManager.bpm.value
            val endTremorB = sensorManager.microTremorScore.value

            _flashPhase.value = FlashTestPhase.COMPLETED
            audioHaptics.stopBinauralTone()

            // Compute human-friendly verdict
            computeAndDisplayVerdict(
                startA = startBpmA, endA = endBpmA, tremorA = endTremorA - initialTremorA,
                startB = startBpmB, endB = endBpmB, tremorB = endTremorB - initialTremorB
            )
        }
    }

    private suspend fun computeAndDisplayVerdict(
        startA: Int, endA: Int, tremorA: Float,
        startB: Int, endB: Int, tremorB: Float
    ) {
        val base = _baselineBpm.value
        val deltaA = endA - base
        val deltaB = endB - base

        val reactionA = when {
            deltaA >= 3 || tremorA > 0.7f -> AutonomicReaction.SYMPATHETIC_SPIKE
            deltaA <= -2 -> AutonomicReaction.PARASYMPATHETIC_RELIEF
            else -> AutonomicReaction.NEUTRAL_EQUILIBRIUM
        }

        val reactionB = when {
            deltaB >= 3 || tremorB > 0.7f -> AutonomicReaction.SYMPATHETIC_SPIKE
            deltaB <= -2 -> AutonomicReaction.PARASYMPATHETIC_RELIEF
            else -> AutonomicReaction.NEUTRAL_EQUILIBRIUM
        }

        val bioA = OptionBiometrics(
            optionKey = "A",
            optionText = _optionA.value,
            initialBpm = startA,
            finalBpm = endA,
            deltaBpm = deltaA,
            tremorScore = tremorA.coerceAtLeast(0f),
            reaction = reactionA
        )

        val bioB = OptionBiometrics(
            optionKey = "B",
            optionText = _optionB.value,
            initialBpm = startB,
            finalBpm = endB,
            deltaBpm = deltaB,
            tremorScore = tremorB.coerceAtLeast(0f),
            reaction = reactionB
        )

        val (winnerKey, winnerText, friendlyInsight) = if (deltaA < deltaB) {
            Triple(
                "A",
                _optionA.value,
                "Your heart settled into a calm, steady rhythm when presented with \"${_optionA.value}\". Meanwhile, Option B triggered defensive physical tension."
            )
        } else {
            Triple(
                "B",
                _optionB.value,
                "Your pulse steadied with relief when presented with \"${_optionB.value}\", while Option A created subconscious resistance and an elevated pulse."
            )
        }

        val verdictData = SomaticVerdictData(
            optionA = _optionA.value,
            optionB = _optionB.value,
            chosenOption = winnerKey,
            chosenText = winnerText,
            baselineBpm = base,
            biometricsA = bioA,
            biometricsB = bioB,
            humanExplanation = friendlyInsight
        )

        val entity = VerdictEntity(
            optionA = verdictData.optionA,
            optionB = verdictData.optionB,
            chosenOption = verdictData.chosenOption,
            chosenText = verdictData.chosenText,
            baselineBpm = verdictData.baselineBpm,
            optionABpm = verdictData.biometricsA.finalBpm,
            optionBBpm = verdictData.biometricsB.finalBpm,
            optionAJitter = verdictData.biometricsA.tremorScore,
            optionBJitter = verdictData.biometricsB.tremorScore,
            optionAReactionType = verdictData.biometricsA.reaction.name,
            optionBReactionType = verdictData.biometricsB.reaction.name,
            reflectionState = null
        )
        val savedId = repository.saveVerdict(entity)
        _currentVerdict.value = verdictData.copy(id = savedId)

        _currentScreen.value = SomaticScreen.VERDICT
    }

    fun submitReflection(answer: ReflectionAnswer) {
        val current = _currentVerdict.value ?: return
        _currentVerdict.value = current.copy(reflection = answer)

        viewModelScope.launch {
            val updated = VerdictEntity(
                id = current.id,
                optionA = current.optionA,
                optionB = current.optionB,
                chosenOption = current.chosenOption,
                chosenText = current.chosenText,
                baselineBpm = current.baselineBpm,
                optionABpm = current.biometricsA.finalBpm,
                optionBBpm = current.biometricsB.finalBpm,
                optionAJitter = current.biometricsA.tremorScore,
                optionBJitter = current.biometricsB.tremorScore,
                optionAReactionType = current.biometricsA.reaction.name,
                optionBReactionType = current.biometricsB.reaction.name,
                reflectionState = answer.name
            )
            repository.updateVerdict(updated)
        }
    }

    fun resetToDilemma() {
        activeTestJob?.cancel()
        audioHaptics.stopBinauralTone()
        sensorManager.stopSensing()
        _currentScreen.value = SomaticScreen.DILEMMA
        _flashPhase.value = FlashTestPhase.IDLE
        _currentVerdict.value = null
    }

    fun deleteHistoryItem(entity: VerdictEntity) {
        viewModelScope.launch {
            repository.deleteVerdict(entity)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    override fun onCleared() {
        super.onCleared()
        heartbeatObserverJob?.cancel()
        activeTestJob?.cancel()
        sensorManager.stopSensing()
        audioHaptics.release()
    }
}
