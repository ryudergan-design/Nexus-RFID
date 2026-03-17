package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalOffer
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun CounterBar(
    readCount: Int,
    foundCount: Int,
    modifier: Modifier = Modifier,
    onReadClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        CounterItem(
            label = "Lidas",
            value = readCount.toString(),
            backgroundColor = AppColors.PrimaryActionBlue.copy(alpha = 0.14f),
            borderColor = AppColors.BrandSignalBlue.copy(alpha = 0.44f),
            contentColor = AppColors.BrandSignalBlue,
            onClick = onReadClick,
            modifier = Modifier.weight(1f)
        )

        CounterItem(
            label = "Encontradas",
            value = foundCount.toString(),
            backgroundColor = AppColors.PositiveGreen.copy(alpha = 0.14f),
            borderColor = AppColors.PositiveGreen.copy(alpha = 0.44f),
            contentColor = AppColors.PositiveGreen,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CounterItem(
    label: String,
    value: String,
    backgroundColor: Color,
    borderColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }
    Column(
        modifier = modifier.then(clickableModifier),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocalOffer,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = AppColors.TextSecondary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .background(backgroundColor, AppShapes.input)
                .border(1.dp, borderColor, AppShapes.input),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
        }
    }
}
