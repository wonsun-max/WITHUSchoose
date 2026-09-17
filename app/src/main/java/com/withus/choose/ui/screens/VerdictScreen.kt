package com.withus.choose.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.withus.choose.ui.ReflectionAnswer
import com.withus.choose.ui.SomaticViewModel
import com.withus.choose.ui.components.TruthCardShareDialog
import com.withus.choose.ui.theme.AmbientBackgroundBrush
import com.withus.choose.ui.theme.GoldWarm
import com.withus.choose.ui.theme.ObsidianBorder
import com.withus.choose.ui.theme.ObsidianCard
import com.withus.choose.ui.theme.ParasympatheticEmerald
import com.withus.choose.ui.theme.SympatheticRed
import com.withus.choose.ui.theme.TextMuted
import com.withus.choose.ui.theme.TextPrimary
import com.withus.choose.ui.theme.TextSecondary

@Composable
fun VerdictScreen(viewModel: SomaticViewModel) {
    val verdict = viewModel.currentVerdict.collectAsState().value ?: return
    var showShareDialog by remember { mutableStateOf(false) }

    if (showShareDialog) {
        TruthCardShareDialog(
            verdict = verdict,
            onDismiss = { showShareDialog = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmbientBackgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(ObsidianCard)
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "THE BODY'S VERDICT",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldWarm,
                    letterSpacing = 1.6.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Dominant Choice Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, GoldWarm.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your nervous system settled on:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = verdict.chosenText,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
                        color = GoldWarm,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ParasympatheticEmerald.copy(alpha = 0.12f))
                            .border(1.dp, ParasympatheticEmerald.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "✨ Physical Relief & Calmer Pulse",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ParasympatheticEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = verdict.humanExplanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Biological Telemetry Card
            Text(
                text = "HOW YOUR BODY REACTED",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.fillMaxWidth(),
                letterSpacing = 1.4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Option A Telemetry
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Path A: ${verdict.optionA}",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = verdict.biometricsA.reaction.displayName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = if (verdict.chosenOption == "A") ParasympatheticEmerald else SympatheticRed
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${verdict.biometricsA.finalBpm} BPM",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (verdict.chosenOption == "A") ParasympatheticEmerald else SympatheticRed
                        )
                        Text(
                            text = "${if (verdict.biometricsA.deltaBpm >= 0) "+${verdict.biometricsA.deltaBpm}" else "${verdict.biometricsA.deltaBpm}"} vs rest",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Option B Telemetry
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Path B: ${verdict.optionB}",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = verdict.biometricsB.reaction.displayName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = if (verdict.chosenOption == "B") ParasympatheticEmerald else SympatheticRed
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${verdict.biometricsB.finalBpm} BPM",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (verdict.chosenOption == "B") ParasympatheticEmerald else SympatheticRed
                        )
                        Text(
                            text = "${if (verdict.biometricsB.deltaBpm >= 0) "+${verdict.biometricsB.deltaBpm}" else "${verdict.biometricsB.deltaBpm}"} vs rest",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // The Reflection Check (Human & Empathetic)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "THE REFLECTION CHECK",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldWarm,
                        letterSpacing = 1.4.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "How does seeing this verdict feel right now?",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (verdict.reflection == null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.submitReflection(ReflectionAnswer.RELIEVED) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ParasympatheticEmerald),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ParasympatheticEmerald.copy(alpha = 0.6f))
                            ) {
                                Text("😌 Relieved", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.submitReflection(ReflectionAnswer.DISAPPOINTED) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SympatheticRed),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SympatheticRed.copy(alpha = 0.6f))
                            ) {
                                Text("😟 Disappointed", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        val feedback = when (verdict.reflection) {
                            ReflectionAnswer.RELIEVED -> "✓ Your conscious mind and nervous system are in total harmony. You have your answer."
                            ReflectionAnswer.DISAPPOINTED -> "💡 Notice that feeling: Disappointment means your heart quietly desired the other path all along!"
                            ReflectionAnswer.NEUTRAL -> "A balanced perspective recorded."
                        }
                        Text(
                            text = feedback,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (verdict.reflection == ReflectionAnswer.RELIEVED) ParasympatheticEmerald else GoldWarm,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary Share CTA & Reset
            Button(
                onClick = { showShareDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldWarm, contentColor = Color(0xFF101015)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Truth Card", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { viewModel.resetToDilemma() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Test Another Decision")
            }
        }
    }
}
