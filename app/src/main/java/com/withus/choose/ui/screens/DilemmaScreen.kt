package com.withus.choose.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.withus.choose.ui.SomaticViewModel
import com.withus.choose.ui.theme.GoldSubtle
import com.withus.choose.ui.theme.GoldWarm
import com.withus.choose.ui.theme.ObsidianBlack
import com.withus.choose.ui.theme.ObsidianBorder
import com.withus.choose.ui.theme.ObsidianCard
import com.withus.choose.ui.theme.TextMuted
import com.withus.choose.ui.theme.TextPrimary
import com.withus.choose.ui.theme.TextSecondary

private val PRESETS = listOf(
    Pair("Accept the corporate job", "Start my own studio"),
    Pair("Move to Chicago", "Stay in New York"),
    Pair("Confront the issue directly", "Let it resolve naturally"),
    Pair("Take the calculated risk", "Protect current stability")
)

@Composable
fun DilemmaScreen(
    viewModel: SomaticViewModel,
    onOpenHistory: () -> Unit
) {
    val optionA by viewModel.optionA.collectAsState()
    val optionB by viewModel.optionB.collectAsState()
    val isMuted by viewModel.audioHaptics.isMuted.collectAsState()

    // Breathing pulse animation for "Consult the Body" CTA button
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WITHUS CHOOSE",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldWarm,
                    letterSpacing = 2.5.sp
                )
                Text(
                    text = "The Somatic Instrument",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.audioHaptics.toggleMute() }) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                        contentDescription = if (isMuted) "Unmute" else "Mute",
                        tint = if (isMuted) TextMuted else GoldWarm
                    )
                }
                IconButton(onClick = onOpenHistory) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Archive",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Center Dilemma Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "What divides you?",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your prefrontal cortex loops in pros & cons.\nYour autonomic nervous system already knows.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = TextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Option A Field
            Text(
                text = "OPTION A",
                style = MaterialTheme.typography.labelSmall,
                color = GoldWarm,
                modifier = Modifier.fillMaxWidth(),
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = optionA,
                onValueChange = { viewModel.setOptionA(it) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldWarm,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedContainerColor = ObsidianCard,
                    unfocusedContainerColor = ObsidianCard,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Enter Option A", color = TextMuted) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Option B Field
            Text(
                text = "OPTION B",
                style = MaterialTheme.typography.labelSmall,
                color = GoldWarm,
                modifier = Modifier.fillMaxWidth(),
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = optionB,
                onValueChange = { viewModel.setOptionB(it) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldWarm,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedContainerColor = ObsidianCard,
                    unfocusedContainerColor = ObsidianCard,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Enter Option B", color = TextMuted) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Presets row
            Text(
                text = "CURATED DILEMMAS",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.fillMaxWidth(),
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(PRESETS) { (presetA, presetB) ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ObsidianCard)
                            .border(1.dp, ObsidianBorder, RoundedCornerShape(8.dp))
                            .clickable { viewModel.applyPreset(presetA, presetB) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${presetA.take(16)}… vs ${presetB.take(16)}…",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Bottom CTA Button: "Consult the Body"
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { viewModel.startCalibration() },
                enabled = optionA.isNotBlank() && optionB.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(pulseScale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldWarm,
                    contentColor = ObsidianBlack,
                    disabledContainerColor = ObsidianBorder,
                    disabledContentColor = TextMuted
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Consult the Body",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "45-Second Biometric Protocol • Zero Sign-Up",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
