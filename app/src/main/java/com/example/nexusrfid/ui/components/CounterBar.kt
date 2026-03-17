package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun CounterBar(
    readCount: Int,
    foundCount: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val itemWidth = (maxWidth - AppSpacing.sm) / 2

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            CounterItem(
                label = "Lidas",
                value = readCount.toString(),
                backgroundColor = AppColors.DarkModal,
                borderColor = AppColors.DarkModal,
                contentColor = AppColors.TopBarOnBlue,
                modifier = Modifier.width(itemWidth)
            )

            CounterItem(
                label = "Encontradas",
                value = foundCount.toString(),
                backgroundColor = AppColors.PositiveGreen,
                borderColor = AppColors.PositiveBorder,
                contentColor = AppColors.TopBarOnBlue,
                modifier = Modifier.width(itemWidth)
            )
        }
    }
}

@Composable
private fun CounterItem(
    label: String,
    value: String,
    backgroundColor: Color,
    borderColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(backgroundColor, AppShapes.card)
            .border(1.dp, borderColor, AppShapes.card)
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = contentColor.copy(alpha = 0.76f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = contentColor
            )
            Text(
                text = if (label == "Lidas") "leituras" else "localizadas",
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.76f)
            )
        }

        Text(
            text = if (label == "Lidas") {
                "Total recebido pelo coletor"
            } else {
                "Tags alvo com leitura recente"
            },
            style = MaterialTheme.typography.bodySmall,
            color = contentColor.copy(alpha = 0.68f)
        )
    }
}
