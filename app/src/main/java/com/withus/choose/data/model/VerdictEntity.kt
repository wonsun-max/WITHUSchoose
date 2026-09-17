package com.withus.choose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persisted record of a Somatic decision test.
 * Records the user's dilemma, the autonomic winner, and biometric telemetry.
 */
@Entity(tableName = "verdicts")
data class VerdictEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val optionA: String,
    val optionB: String,
    val chosenOption: String, // "A" or "B"
    val chosenText: String,
    val baselineBpm: Int,
    val optionABpm: Int,
    val optionBBpm: Int,
    val optionAJitter: Float,
    val optionBJitter: Float,
    val optionAReactionType: String, // "SYMPATHETIC_SPIKE", "PARASYMPATHETIC_RELIEF", etc.
    val optionBReactionType: String,
    val reflectionState: String? = null // "RELIEVED", "DISAPPOINTED", "NEUTRAL"
)
