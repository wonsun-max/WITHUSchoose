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
import androidx.compose.foundation.layout.width
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
import com.withus.choose.ui.theme.AmbientBackgroundBrush
import com.withus.choose.ui.theme.GoldWarm
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmbientBackgroundBrush)
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
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "STEP 2 OF 2 : THE INSTINCT",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = GoldWarm,
                        letterSpacing = 1.4.sp
                    )
                }

                IconButton(
                    onClick = { viewModel.resetToDilemma() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ObsidianCard)
                        .size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Central Stage
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
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 88.sp),
                                fontWeight = FontWeight.Normal,
                                color = GoldWarm
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Keep your finger still.\nRest your eyes on the center.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = TextSecondary,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    FlashTestPhase.UNPREDICTABLE_SILENCE_A,
                    FlashTestPhase.UNPREDICTABLE_SILENCE_B -> {
                        // Ambient focus node
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(GoldWarm.copy(alpha = 0.4f))
                        )
                    }

                    FlashTestPhase.FLASH_A,
                    FlashTestPhase.FLASH_B -> {
                        // Clean, high-impact choice card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFF161622))
                                .border(1.5.dp, GoldWarm, RoundedCornerShape(24.dp))
                                .padding(horizontal = 24.dp, vertical = 36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (phase == FlashTestPhase.FLASH_A) "PATH A" else "PATH B",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = GoldWarm,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = activeFlashText,
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 30.sp),
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 38.sp
                            )
                        }
                    }

                    FlashTestPhase.IMPULSE_A,
                    FlashTestPhase.IMPULSE_B -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(GoldWarm)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Listening to your body's instinct…",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        }
                    }

                    FlashTestPhase.RESET_PAUSE -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Take a slow, deep breath.",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Let your shoulders drop ($resetSeconds)",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                color = ParasympatheticEmerald
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = "Unveiling your somatic verdict…",
                            style = MaterialTheme.typography.bodyLarge,
                            color = GoldWarm
                        )
                    }
                }
            }

            // Bottom Pulse Telemetry Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(ObsidianCard)
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AUTONOMIC SENSING",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        letterSpacing = 1.2.sp
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
