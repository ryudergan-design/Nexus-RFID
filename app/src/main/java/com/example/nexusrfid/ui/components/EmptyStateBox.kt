package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun EmptyStateBox(
    title: String,
    supportingText: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.CardSurface, AppShapes.card)
            .border(1.dp, AppColors.Divider, AppShapes.card)
            .padding(AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.TextPrimary
        )

        if (!supportingText.isNullOrBlank()) {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary
            )
        }

        if (!actionLabel.isNullOrBlank() && onAction != null) {
            ActionButtonOutline(
                text = actionLabel,
                onClick = onAction,
                borderColor = AppColors.PositiveGreen,
                contentColor = AppColors.PositiveGreen
            )
        }
    }
}
