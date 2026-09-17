package com.withus.choose.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.withus.choose.ui.theme.GoldWarm
import com.withus.choose.ui.theme.WaveformCyan
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PpgWaveformCanvas(
    waveformStream: SharedFlow<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = WaveformCyan,
    maxPoints: Int = 100
) {
    val points = remember { mutableStateListOf<Float>() }

    LaunchedEffect(waveformStream) {
        waveformStream.collectLatest { point ->
            if (points.size >= maxPoints) {
                points.removeAt(0)
            }
            points.add(point)
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        if (points.size < 2) return@Canvas

        val width = size.width
        val height = size.height
        val stepX = width / (maxPoints - 1).coerceAtLeast(1)

        val path = Path()
        val startY = height - (points[0].coerceIn(0f, 1f) * height)
        path.moveTo(0f, startY)

        for (i in 1 until points.size) {
            val x = i * stepX
            val y = height - (points[i].coerceIn(0f, 1f) * height)
            path.lineTo(x, y)
        }

        // Draw glowing line with subtle gradient
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.1f),
                    lineColor.copy(alpha = 0.6f),
                    lineColor
                )
            ),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw leading pulse dot at the tip of the waveform
        val lastIdx = points.size - 1
        val lastX = lastIdx * stepX
        val lastY = height - (points[lastIdx].coerceIn(0f, 1f) * height)

        drawCircle(
            color = GoldWarm,
            radius = 4.dp.toPx(),
            center = Offset(lastX, lastY)
        )
        drawCircle(
            color = GoldWarm.copy(alpha = 0.35f),
            radius = 8.dp.toPx(),
            center = Offset(lastX, lastY)
        )
    }
}
