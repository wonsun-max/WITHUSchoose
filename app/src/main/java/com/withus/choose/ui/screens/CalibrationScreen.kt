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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
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
fun CalibrationScreen(viewModel: SomaticViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val progress by viewModel.calibrationProgress.collectAsState()
    val secondsRemaining by viewModel.calibrationSecondsRemaining.collectAsState()
    val liveBpm by viewModel.sensorManager.bpm.collectAsState()
    val isFingerDetected by viewModel.sensorManager.isFingerDetected.collectAsState()
    val isSimulated by viewModel.sensorManager.isSimulated.collectAsState()

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "calibrationProgress")

    DisposableEffect(lifecycleOwner) {
        viewModel.sensorManager.startSensing(lifecycleOwner)
        onDispose {
            viewModel.sensorManager.stopSensing()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .pointerInteropFilter { motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_MOVE || motionEvent.action == MotionEvent.ACTION_DOWN) {
                    viewModel.sensorManager.recordTouchEvent(
                        motionEvent.x,
                        motionEvent.y,
                        motionEvent.pressure
                    )
                }
                false
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.resetToDilemma() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextSecondary
                )
            }
            Text(
                text = "PHASE 01 : CALIBRATION",
                style = MaterialTheme.typography.labelSmall,
                color = GoldWarm,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        // Center Calibration Ring & Prompts
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Place your index finger gently\nover the rear camera and flash.",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Circular Progress Ring with live BPM in center
            Box(
                modifier = Modifier.size(220.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Track Ring
                Canvas(modifier = Modifier.size(220.dp)) {
                    drawArc(
                        color = ObsidianBorder,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Animated Active Ring
                Canvas(modifier = Modifier.size(220.dp)) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(GoldWarm.copy(alpha = 0.6f), GoldWarm, ParasympatheticEmerald)
                        ),
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Center Telemetry
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Heartbeat",
                        tint = GoldWarm,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$liveBpm",
                        style = MaterialTheme.typography.displayLarge,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "RESTING BPM",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${secondsRemaining}s remaining",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = GoldWarm
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Calibrating resting nervous system…\ntake one slow, deep breath.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = TextSecondary
            )

            if (isSimulated) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "EMULATOR / TEST MODE ACTIVE",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = GoldWarm
                    )
                }
            }
        }

        // Bottom Live PPG Waveform visualizer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ObsidianCard)
                .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ARTERIAL PULSE WAVE (PPG)",
                    style = MaterialTheme.typography.labelSmall,
                    color = WaveformCyan,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = if (isFingerDetected) "SENSOR LOCKED" else "POSITION FINGER",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = if (isFingerDetected) ParasympatheticEmerald else GoldWarm
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            PpgWaveformCanvas(
                waveformStream = viewModel.sensorManager.ppgWaveformPoint,
                modifier = Modifier.height(60.dp)
            )
        }
    }
}
