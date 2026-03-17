package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
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
    selectedKey: String,
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
                .border(1.dp, AppColors.Divider, AppShapes.modal)
                .padding(vertical = AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Text(
                text = "Tipo de busca",
                modifier = Modifier.padding(horizontal = AppSpacing.lg),
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.TextPrimary
            )
            Text(
                text = "Escolha como deseja localizar os produtos.",
                modifier = Modifier.padding(horizontal = AppSpacing.lg),
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary
            )

            options.forEach { option ->
                SearchTypeRow(
                    option = option,
                    selected = option.key == selectedKey,
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}

@Composable
private fun SearchTypeRow(
    option: SearchTypeOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = if (selected) AppColors.AccentSurface else AppColors.CardSurface
            )
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md)
    ) {
        Text(
            text = option.label,
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) AppColors.TopBarBlue else AppColors.TextPrimary
        )

        if (selected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd),
                tint = AppColors.PrimaryActionBlue
            )
        }
    }
}
