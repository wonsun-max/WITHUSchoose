package com.withus.choose.ui.screens

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun CalibrationScreen(viewModel: SomaticViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val progress by viewModel.calibrationProgress.collectAsState()
    val secondsRemaining by viewModel.calibrationSecondsRemaining.collectAsState()
    val liveBpm by viewModel.sensorManager.bpm.collectAsState()
    val isFingerDetected by viewModel.sensorManager.isFingerDetected.collectAsState()
    val isSimulated by viewModel.sensorManager.isSimulated.collectAsState()

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "calibrationRing")

    DisposableEffect(lifecycleOwner) {
        viewModel.sensorManager.startSensing(lifecycleOwner)
        onDispose {
            viewModel.sensorManager.stopSensing()
        }
    }

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
                IconButton(
                    onClick = { viewModel.resetToDilemma() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ObsidianCard)
                        .size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "STEP 1 OF 2 : BASELINE",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = GoldWarm,
                        letterSpacing = 1.4.sp
                    )
                }

                Spacer(modifier = Modifier.size(38.dp))
            }

            // Center Ring & Coaching
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Rest your finger gently\nover the camera.",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "A light, relaxed touch works best. Don't press hard.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Progress Ring with Heartbeat
                Box(
                    modifier = Modifier.size(230.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer track
                    Canvas(modifier = Modifier.size(230.dp)) {
                        drawArc(
                            color = ObsidianBorder,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Active filling ring
                    Canvas(modifier = Modifier.size(230.dp)) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(
                                    GoldWarm.copy(alpha = 0.5f),
                                    GoldWarm,
                                    ParasympatheticEmerald
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = 360f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Inside Telemetry
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Pulse",
                            tint = if (isFingerDetected) ParasympatheticEmerald else GoldWarm,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$liveBpm",
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 42.sp),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "RESTING BPM",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${secondsRemaining}s left",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = GoldWarm
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Friendly Status Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isFingerDetected) ParasympatheticEmerald else GoldWarm)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFingerDetected) "Pulse locked • Breathe slowly" else "Place fingertip over camera lens",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                if (isSimulated) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Emulator preview mode active",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
            }

            // Bottom Waveform Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(ObsidianCard)
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE PULSE WAVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = WaveformCyan,
                        letterSpacing = 1.4.sp
                    )
                    Text(
                        text = "REAL-TIME PPG",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                PpgWaveformCanvas(
                    waveformStream = viewModel.sensorManager.ppgWaveformPoint,
                    modifier = Modifier.height(55.dp)
                )
            }
        }
    }
}
