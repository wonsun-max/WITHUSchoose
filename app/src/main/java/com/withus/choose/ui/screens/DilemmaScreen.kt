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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SwapVert
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
import com.withus.choose.ui.theme.AmbientBackgroundBrush
import com.withus.choose.ui.theme.GoldGlow
import com.withus.choose.ui.theme.GoldLight
import com.withus.choose.ui.theme.GoldWarm
import com.withus.choose.ui.theme.ObsidianBorder
import com.withus.choose.ui.theme.ObsidianCard
import com.withus.choose.ui.theme.ObsidianElevated
import com.withus.choose.ui.theme.TextMuted
import com.withus.choose.ui.theme.TextPrimary
import com.withus.choose.ui.theme.TextSecondary

private data class DilemmaPreset(
    val title: String,
    val optionA: String,
    val optionB: String
)

private val PRESET_LIST = listOf(
    DilemmaPreset("💼 Career", "Accept the corporate job", "Start my own studio"),
    DilemmaPreset("📍 Location", "Move to Chicago", "Stay in New York"),
    DilemmaPreset("💬 Honesty", "Speak my honest feelings", "Let it pass in silence"),
    DilemmaPreset("🌱 Growth", "Take the bold leap", "Preserve current comfort")
)

@Composable
fun DilemmaScreen(
    viewModel: SomaticViewModel,
    onOpenHistory: () -> Unit
) {
    val optionA by viewModel.optionA.collectAsState()
    val optionB by viewModel.optionB.collectAsState()
    val isMuted by viewModel.audioHaptics.isMuted.collectAsState()

    // Breathing pulse animation for CTA button
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmbientBackgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "WITHUS CHOOSE",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldWarm,
                        letterSpacing = 1.6.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.audioHaptics.toggleMute() },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(ObsidianCard)
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Sound off" else "Sound on",
                            tint = if (isMuted) TextMuted else GoldWarm,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = onOpenHistory,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(ObsidianCard)
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Decisions Archive",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Hero Typography (Human & Empathetic)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Listen to your body.",
                    style = MaterialTheme.typography.displayLarge,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "When your mind is trapped weighing pros & cons, your autonomic nervous system already knows what you truly want.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Inputs Card with Swap Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Option A Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "FIRST PATH",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldWarm,
                            letterSpacing = 1.2.sp
                        )
                        if (optionA.isNotBlank()) {
                            Text(
                                text = "Clear",
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier.clickable { viewModel.setOptionA("") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = optionA,
                        onValueChange = { viewModel.setOptionA(it) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        placeholder = { Text("e.g. Accept the corporate job", color = TextMuted) },
                        textStyle = MaterialTheme.typography.titleMedium,
                        singleLine = true
                    )
                }

                // Interactive Swap Pill
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ObsidianElevated)
                        .border(1.dp, ObsidianBorder, CircleShape)
                        .clickable { viewModel.swapOptions() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Swap options",
                        tint = GoldWarm,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Option B Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(ObsidianCard)
                        .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SECOND PATH",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldWarm,
                            letterSpacing = 1.2.sp
                        )
                        if (optionB.isNotBlank()) {
                            Text(
                                text = "Clear",
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier.clickable { viewModel.setOptionB("") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = optionB,
                        onValueChange = { viewModel.setOptionB(it) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        placeholder = { Text("e.g. Start my own studio", color = TextMuted) },
                        textStyle = MaterialTheme.typography.titleMedium,
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Friendly Curated Presets
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "NEED INSPIRATION?",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(PRESET_LIST) { preset ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ObsidianCard)
                                .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
                                .clickable { viewModel.applyPreset(preset.optionA, preset.optionB) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = preset.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Action CTA: "Consult the Body"
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.startCalibration() },
                    enabled = optionA.isNotBlank() && optionB.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .scale(pulseScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldWarm,
                        contentColor = Color(0xFF101015),
                        disabledContainerColor = ObsidianCard,
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Consult the Body",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "45-second scientific reflex test • 100% on-device & private",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }
    }
}
