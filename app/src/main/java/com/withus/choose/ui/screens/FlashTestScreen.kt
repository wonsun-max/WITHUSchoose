package com.withus.choose.ui.screens

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.withus.choose.ui.FlashTestPhase
import com.withus.choose.ui.SomaticViewModel
import com.withus.choose.ui.components.PpgWaveformCanvas
import com.withus.choose.ui.theme.GoldWarm
import com.withus.choose.ui.theme.ObsidianBlack
import com.withus.choose.ui.theme.ObsidianBorder
import com.withus.choose.ui.theme.ObsidianCard
import com.withus.choose.ui.theme.ParasympatheticEmerald
import com.withus.choose.ui.theme.TextMuted
import com.withus.choose.ui.theme.TextPrimary
import com.withus.choose.ui.theme.TextSecondary
import com.withus.choose.ui.theme.WaveformCyan

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FlashTestScreen(viewModel: SomaticViewModel) {
    val phase by viewModel.flashPhase.collectAsState()
    val countdownNumber by viewModel.countdownNumber.collectAsState()
    val activeFlashText by viewModel.activeFlashText.collectAsState()
    val resetSeconds by viewModel.resetSecondsRemaining.collectAsState()
    val liveBpm by viewModel.sensorManager.bpm.collectAsState()

    // Touch motion filter to record finger tremor
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .pointerInteropFilter { motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_MOVE || motionEvent.action == MotionEvent.ACTION_DOWN) {
                    viewModel.sensorManager.recordTouchEvent(
                        motionEvent.x,
                        motionEvent.y,
                        motionEvent.pressure
                    )
                }
                false
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PHASE 02 : THE FLASH MOMENT",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldWarm,
                    letterSpacing = 2.sp
                )
                IconButton(onClick = { viewModel.resetToDilemma() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Abort",
                        tint = TextMuted
                    )
                }
            }

            // Main Suspense Center Stage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (phase) {
                    FlashTestPhase.COUNTDOWN -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$countdownNumber",
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 96.sp),
                                fontWeight = FontWeight.ExtraLight,
                                color = GoldWarm
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Keep your finger still.\nFixate on the center.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = TextSecondary
                            )
                        }
                    }

                    FlashTestPhase.UNPREDICTABLE_SILENCE_A,
                    FlashTestPhase.UNPREDICTABLE_SILENCE_B -> {
                        // Pitch black suspense: Prevents conscious bracing
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(TextMuted.copy(alpha = 0.3f))
                        )
                    }

                    FlashTestPhase.FLASH_A,
                    FlashTestPhase.FLASH_B -> {
                        // High-contrast 0.5s sudden stimulus flash
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (phase == FlashTestPhase.FLASH_A) "OPTION A" else "OPTION B",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldWarm,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = activeFlashText,
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 42.sp
                            )
                        }
                    }

                    FlashTestPhase.IMPULSE_A,
                    FlashTestPhase.IMPULSE_B -> {
                        // 3.0s Acute Impulse Window: Pitch black recording orienting response
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(GoldWarm)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Recording Autonomic Orienting Reflex…",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    FlashTestPhase.RESET_PAUSE -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Resetting autonomic baseline…",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Exhale slowly ($resetSeconds)",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                color = ParasympatheticEmerald
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = "Synthesizing Somatic Verdict…",
                            style = MaterialTheme.typography.bodyLarge,
                            color = GoldWarm
                        )
                    }
                }
            }

            // Bottom Minimal Biometric Waveform
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ObsidianCard)
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AUTONOMIC TELEMETRY",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$liveBpm BPM",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = GoldWarm
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                PpgWaveformCanvas(
                    waveformStream = viewModel.sensorManager.ppgWaveformPoint,
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}
