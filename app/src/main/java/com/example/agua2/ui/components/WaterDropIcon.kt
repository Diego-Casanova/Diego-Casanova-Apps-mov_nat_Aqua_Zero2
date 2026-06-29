package com.example.agua2.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun WaterDropIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF3B82F6)
) {
    Canvas(modifier = modifier.size(100.dp)) {
        val width = size.width
        val height = size.height
        
        val path = Path().apply {
            moveTo(width / 2f, 0f)
            cubicTo(
                width / 2f, 0f,
                width, height * 0.6f,
                width, height * 0.75f
            )
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, height * 0.5f, width, height),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            cubicTo(
                0f, height * 0.6f,
                width / 2f, 0f,
                width / 2f, 0f
            )
            close()
        }
        drawPath(path, color = color)
    }
}
