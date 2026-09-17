package com.withus.choose.ui.components

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.withus.choose.ui.SomaticVerdictData
import com.withus.choose.ui.theme.GoldWarm
import com.withus.choose.ui.theme.ObsidianBlack
import com.withus.choose.ui.theme.ObsidianBorder
import com.withus.choose.ui.theme.ObsidianCard
import com.withus.choose.ui.theme.ParasympatheticEmerald
import com.withus.choose.ui.theme.SympatheticRed
import com.withus.choose.ui.theme.TextMuted
import com.withus.choose.ui.theme.TextPrimary
import com.withus.choose.ui.theme.TextSecondary
import java.io.File
import java.io.FileOutputStream

@Composable
fun TruthCardShareDialog(
    verdict: SomaticVerdictData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = ObsidianBlack)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRUTH CARD",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldWarm,
                        letterSpacing = 2.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 9:16 Preview Card Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(9f / 14f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ObsidianCard)
                        .border(1.dp, GoldWarm.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Brand / Tag
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "WITHUS CHOOSE",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                letterSpacing = 2.5.sp
                            )
                            Text(
                                text = "SOMATIC MARKER INSTRUMENT",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = GoldWarm.copy(alpha = 0.7f)
                            )
                        }

                        // Middle Viral Hook
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = "“My brain argued for weeks.\nMy nervous system took 0.4s to choose.”",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                color = TextPrimary,
                                lineHeight = 28.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ObsidianBlack)
                                    .border(1.dp, ObsidianBorder, RoundedCornerShape(10.dp))
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = verdict.chosenText,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = GoldWarm,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Bottom Biometric Telemetry
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0E0E14))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Option A: ${verdict.biometricsA.finalBpm} BPM",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (verdict.chosenOption == "A") ParasympatheticEmerald else SympatheticRed
                                )
                                Text(
                                    text = "Option B: ${verdict.biometricsB.finalBpm} BPM",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (verdict.chosenOption == "B") ParasympatheticEmerald else SympatheticRed
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Baseline: ${verdict.baselineBpm} BPM • Antonio Damasio Reflex Verified",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextMuted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // System Share Button
                Button(
                    onClick = {
                        shareTruthCard(context, verdict)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldWarm, contentColor = ObsidianBlack),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Share Truth Card", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun shareTruthCard(context: Context, verdict: SomaticVerdictData) {
    val shareText = """
        ⚡ WITHUS Choose: Somatic Biometric Verdict
        
        "My brain argued for weeks. My nervous system took 0.4s to decide."
        
        ✦ The Verdict: ${verdict.chosenText}
        ✦ Telemetry:
          • Option A (${verdict.optionA}): ${verdict.biometricsA.finalBpm} BPM (${if (verdict.biometricsA.deltaBpm >= 0) "+${verdict.biometricsA.deltaBpm}" else "${verdict.biometricsA.deltaBpm}"} BPM)
          • Option B (${verdict.optionB}): ${verdict.biometricsB.finalBpm} BPM (${if (verdict.biometricsB.deltaBpm >= 0) "+${verdict.biometricsB.deltaBpm}" else "${verdict.biometricsB.deltaBpm}"} BPM)
          • Baseline Resting: ${verdict.baselineBpm} BPM
        
        Biometrically verified via Antonio Damasio's Somatic Marker Hypothesis.
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Somatic Marker Decision Verdict")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share Somatic Truth Card"))
}
