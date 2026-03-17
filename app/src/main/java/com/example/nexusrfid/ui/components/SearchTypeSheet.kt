package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.SearchTypeOption
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun SearchTypeSheet(
    options: List<SearchTypeOption>,
    onSelect: (SearchTypeOption) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.20f))
                .clickable(onClick = onDismiss)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(AppColors.CardSurface, AppShapes.modal)
                .padding(vertical = AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(
                text = "Voce deseja buscar pelo que?",
                modifier = Modifier.padding(horizontal = AppSpacing.lg),
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextPrimary
            )
            HorizontalDivider(color = AppColors.Divider, modifier = Modifier.padding(top = AppSpacing.sm))
            options.forEachIndexed { index, option ->
                SearchTypeRow(
                    option = option,
                    onClick = { onSelect(option) }
                )
                if (index != options.lastIndex) {
                    HorizontalDivider(color = AppColors.Divider)
                }
            }
        }
    }
}

@Composable
private fun SearchTypeRow(
    option: SearchTypeOption,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(color = AppColors.CardSurface)
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm)
    ) {
        Text(
            text = option.label,
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.TopBarBlue
        )
    }
}
