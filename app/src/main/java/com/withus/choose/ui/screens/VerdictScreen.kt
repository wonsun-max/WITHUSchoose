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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import com.withus.choose.ui.theme.GoldWarm
import com.withus.choose.ui.theme.ObsidianBlack
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "SOMATIC VERDICT",
            style = MaterialTheme.typography.labelSmall,
            color = GoldWarm,
            letterSpacing = 2.5.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dominant Choice Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, GoldWarm.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your nervous system chose:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = verdict.chosenText,
                    style = MaterialTheme.typography.displayMedium,
                    color = GoldWarm,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ParasympatheticEmerald.copy(alpha = 0.15f))
                        .border(1.dp, ParasympatheticEmerald.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "AUTONOMIC ORIENTING RELIEF",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = ParasympatheticEmerald
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Biological Data Breakdown
        Text(
            text = "BIOLOGICAL TELEMETRY",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            modifier = Modifier.fillMaxWidth(),
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Option A Telemetry Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "OPTION A: ${verdict.optionA}",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = verdict.biometricsA.reaction.description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${verdict.biometricsA.finalBpm} BPM",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (verdict.chosenOption == "A") ParasympatheticEmerald else SympatheticRed
                    )
                    Text(
                        text = "${if (verdict.biometricsA.deltaBpm >= 0) "+${verdict.biometricsA.deltaBpm}" else "${verdict.biometricsA.deltaBpm}"} BPM shift",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Option B Telemetry Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "OPTION B: ${verdict.optionB}",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = verdict.biometricsB.reaction.description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${verdict.biometricsB.finalBpm} BPM",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (verdict.chosenOption == "B") ParasympatheticEmerald else SympatheticRed
                    )
                    Text(
                        text = "${if (verdict.biometricsB.deltaBpm >= 0) "+${verdict.biometricsB.deltaBpm}" else "${verdict.biometricsB.deltaBpm}"} BPM shift",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Secondary Reflection Check
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp)),
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
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Did seeing this verdict make you feel relieved, or disappointed?",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
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
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ParasympatheticEmerald),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ParasympatheticEmerald)
                        ) {
                            Text("Relieved", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.submitReflection(ReflectionAnswer.DISAPPOINTED) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SympatheticRed),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SympatheticRed)
                        ) {
                            Text("Disappointed", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    val reflectionText = when (verdict.reflection) {
                        ReflectionAnswer.RELIEVED -> "✓ Verified: Relief confirms deep somatic alignment with this choice."
                        ReflectionAnswer.DISAPPOINTED -> "⚠ Subconscious Insight: Disappointment reveals your prefrontal mind desired the opposite outcome!"
                        ReflectionAnswer.NEUTRAL -> "Neutral balance recorded."
                    }
                    Text(
                        text = reflectionText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (verdict.reflection == ReflectionAnswer.RELIEVED) ParasympatheticEmerald else GoldWarm,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Actions: Share Truth Card & Test Another Dilemma
        Button(
            onClick = { showShareDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GoldWarm, contentColor = ObsidianBlack),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Viral Truth Card", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { viewModel.resetToDilemma() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Test Another Dilemma")
        }
    }
}
