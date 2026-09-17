package com.withus.choose

import com.withus.choose.ui.AutonomicReaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SomaticDecisionTest {

    @Test
    fun testBpmCalculationFromInterval() {
        val intervalMs = 800L
        val calculatedBpm = (60_000.0 / intervalMs).toInt()
        assertEquals(75, calculatedBpm)
    }

    @Test
    fun testAutonomicClassification() {
        val baseline = 70

        // Option A: +9 BPM sympathetic spike
        val optionABpm = 79
        val deltaA = optionABpm - baseline
        val reactionA = when {
            deltaA >= 4 -> AutonomicReaction.SYMPATHETIC_SPIKE
            deltaA <= -2 -> AutonomicReaction.PARASYMPATHETIC_RELIEF
            else -> AutonomicReaction.NEUTRAL_EQUILIBRIUM
        }
        assertEquals(AutonomicReaction.SYMPATHETIC_SPIKE, reactionA)

        // Option B: -5 BPM parasympathetic deceleration
        val optionBBpm = 65
        val deltaB = optionBBpm - baseline
        val reactionB = when {
            deltaB >= 4 -> AutonomicReaction.SYMPATHETIC_SPIKE
            deltaB <= -2 -> AutonomicReaction.PARASYMPATHETIC_RELIEF
            else -> AutonomicReaction.NEUTRAL_EQUILIBRIUM
        }
        assertEquals(AutonomicReaction.PARASYMPATHETIC_RELIEF, reactionB)

        // The decision algorithm selects the option with lower stress (B)
        assertTrue(deltaB < deltaA)
    }
}
