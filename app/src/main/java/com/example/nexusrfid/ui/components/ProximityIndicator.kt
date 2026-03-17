package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun ProximityIndicator(
    title: String,
    percent: Int,
    label: String,
    supportingText: String,
    modifier: Modifier = Modifier
) {
    val clampedPercent = percent.coerceIn(0, 100)
    val highlightColor = proximityColorFor(clampedPercent)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
        border = BorderStroke(1.dp, AppColors.AccentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                    Text(
                        text = "SINAL RFID",
                        style = MaterialTheme.typography.labelLarge,
                        color = AppColors.TextSecondary
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.TextPrimary
                    )
                }

                Text(
                    text = "$clampedPercent%",
                    style = MaterialTheme.typography.headlineSmall,
                    color = highlightColor
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    Text(
                        text = label.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = highlightColor
                    )
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary
                    )
                }

                Row(
                    modifier = Modifier
                        .background(highlightColor.copy(alpha = 0.10f), AppShapes.card)
                        .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                ) {
                    Text(
                        text = if (clampedPercent <= 33) "Fora do eixo" else if (clampedPercent <= 66) "Na area" else "Muito proximo",
                        style = MaterialTheme.typography.bodySmall,
                        color = highlightColor
                    )
                }
            }

            LinearProgressIndicator(
                progress = { clampedPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = highlightColor,
                trackColor = AppColors.MutedSurface
            )
        }
    }
}

fun proximityColorFor(percent: Int): Color {
    return when (percent.coerceIn(0, 100)) {
        in 0..33 -> Color(0xFFD35454)
        in 34..66 -> Color(0xFFCF9E24)
        else -> Color(0xFF3E9B63)
    }
}
