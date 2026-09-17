package com.withus.choose.ui

enum class SomaticScreen {
    DILEMMA,
    CALIBRATION,
    FLASH_TEST,
    VERDICT
}

enum class AutonomicReaction(val displayName: String, val description: String) {
    PARASYMPATHETIC_RELIEF(
        "Physical Relief & Ease",
        "Your pulse steadied and slowed—the body's natural signature of inner peace and acceptance."
    ),
    SYMPATHETIC_SPIKE(
        "Subconscious Tension",
        "An acute bump in heart rate and micro-finger recoil—your nervous system bracing against resistance."
    ),
    NEUTRAL_EQUILIBRIUM(
        "Steady Neutral",
        "Minimal autonomic change—neither strong attraction nor resistance."
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
    val humanExplanation: String,
    val reflection: ReflectionAnswer? = null
)
