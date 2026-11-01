package com.gogart.bmicalculator.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gogart.bmicalculator.data.BmiCategory

@Composable
fun BmiScaleCard(
    bmiValue: Float,
    category: BmiCategory,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Цифрова шкала ІМТ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            val minScale = 15.0f
            val maxScale = 42.0f
            val clampedBmi = bmiValue.coerceIn(minScale, maxScale)
            val rawFraction = (clampedBmi - minScale) / (maxScale - minScale)

            val animatedFraction by animateFloatAsState(
                targetValue = rawFraction,
                animationSpec = tween(durationMillis = 800),
                label = "scale_pointer"
            )

            val pointerColor = category.color
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                val width = size.width
                val barHeight = 18.dp.toPx()
                val barTop = 28.dp.toPx()

                val totalRange = maxScale - minScale

                val segments = listOf(
                    0f to (18.5f - minScale) / totalRange to Color(0xFF2196F3),
                    (18.5f - minScale) / totalRange to (25.0f - minScale) / totalRange to Color(0xFF4CAF50),
                    (25.0f - minScale) / totalRange to (30.0f - minScale) / totalRange to Color(0xFFFF9800),
                    (30.0f - minScale) / totalRange to (35.0f - minScale) / totalRange to Color(0xFFFF5722),
                    (35.0f - minScale) / totalRange to (40.0f - minScale) / totalRange to Color(0xFFF44336),
                    (40.0f - minScale) / totalRange to 1.0f to Color(0xFFB71C1C)
                )

                drawRoundRect(
                    color = Color.LightGray.copy(alpha = 0.2f),
                    topLeft = Offset(0f, barTop),
                    size = Size(width, barHeight),
                    cornerRadius = CornerRadius(barHeight / 2, barHeight / 2)
                )

                for (seg in segments) {
                    val startX = seg.first.first * width
                    val endX = seg.first.second * width
                    val segWidth = (endX - startX).coerceAtLeast(1f)

                    drawRoundRect(
                        color = seg.second,
                        topLeft = Offset(startX, barTop),
                        size = Size(segWidth, barHeight)
                    )
                }

                val pointerX = (animatedFraction * width).coerceIn(8f, width - 8f)
                val pointerPath = Path().apply {
                    moveTo(pointerX, barTop - 2f)
                    lineTo(pointerX - 12f, barTop - 18f)
                    lineTo(pointerX + 12f, barTop - 18f)
                    close()
                }

                drawPath(path = pointerPath, color = pointerColor)

                drawCircle(
                    color = pointerColor,
                    radius = 5.dp.toPx(),
                    center = Offset(pointerX, barTop - 18f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("15", "18.5", "25", "30", "35", "40+").forEach { mark ->
                    Text(
                        text = mark,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(category.color.copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(category.color)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "${category.titleUa} (${category.rangeText})",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = category.color
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.recommendation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}
