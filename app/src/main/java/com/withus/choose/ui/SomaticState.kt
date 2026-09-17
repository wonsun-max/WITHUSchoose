package com.withus.choose.ui

import com.withus.choose.data.model.VerdictEntity

enum class SomaticScreen {
    DILEMMA,
    CALIBRATION,
    FLASH_TEST,
    VERDICT
}

enum class AutonomicReaction(val displayName: String, val description: String) {
    PARASYMPATHETIC_RELIEF(
        "Parasympathetic Stabilization",
        "Heart rate deceleration & rhythmic calm (biomarker of intuitive relief/acceptance)"
    ),
    SYMPATHETIC_SPIKE(
        "Sympathetic Spike",
        "Acute BPM surge & micro-tremor (biomarker of subconscious recoil/resistance)"
    ),
    NEUTRAL_EQUILIBRIUM(
        "Neutral Equilibrium",
        "Minimal autonomic fluctuation"
    )
}

enum class FlashTestPhase {
    IDLE,
    COUNTDOWN,
    UNPREDICTABLE_SILENCE_A,
    FLASH_A,
    IMPULSE_A,
    RESET_PAUSE,
    UNPREDICTABLE_SILENCE_B,
    FLASH_B,
    IMPULSE_B,
    COMPLETED
}

enum class ReflectionAnswer {
    RELIEVED,
    DISAPPOINTED,
    NEUTRAL
}

data class OptionBiometrics(
    val optionKey: String, // "A" or "B"
    val optionText: String,
    val initialBpm: Int,
    val finalBpm: Int,
    val deltaBpm: Int,
    val tremorScore: Float,
    val reaction: AutonomicReaction
)

data class SomaticVerdictData(
    val id: Long = 0,
    val optionA: String,
    val optionB: String,
    val chosenOption: String, // "A" or "B"
    val chosenText: String,
    val baselineBpm: Int,
    val biometricsA: OptionBiometrics,
    val biometricsB: OptionBiometrics,
    val scientificRationale: String,
    val reflection: ReflectionAnswer? = null
)
