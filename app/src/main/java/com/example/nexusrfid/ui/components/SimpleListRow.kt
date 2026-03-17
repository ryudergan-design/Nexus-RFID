package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun SimpleListRow(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(if (selected) AppColors.CardSurfaceHighlight else Color.Transparent)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .defaultMinSize(minHeight = 56.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = AppSpacing.lg,
                vertical = AppSpacing.md
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) AppColors.BrandSignalBlue else AppColors.TopBarOnBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                color = AppColors.Divider,
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = AppSpacing.lg)
            )
        }
    }
}
