package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

data class TagTargetItemUi(
    val epc: String,
    val proximityPercent: Int,
    val proximityLabel: String,
    val matchedProductName: String? = null,
    val lastSeenAtMillis: Long? = null
)

@Composable
fun TagTargetList(
    items: List<TagTargetItemUi>,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
        border = BorderStroke(1.dp, AppColors.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = AppSpacing.xxs)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                TagTargetRow(
                    item = item,
                    showDivider = index < items.lastIndex,
                    onRemove = { onRemove(item.epc) }
                )
            }
        }
    }
}

@Composable
private fun TagTargetRow(
    item: TagTargetItemUi,
    showDivider: Boolean,
    onRemove: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
            ) {
                Text(
                    text = item.epc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary
                )
                Text(
                    text = buildString {
                        append(item.proximityLabel)
                        append("  |  ")
                        append("${item.proximityPercent}%")
                        if (!item.matchedProductName.isNullOrBlank()) {
                            append("  |  ")
                            append(item.matchedProductName)
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Remover tag",
                    tint = AppColors.TextSecondary
                )
            }
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg)
            ) {
                androidx.compose.material3.HorizontalDivider(color = AppColors.Divider)
            }
        }
    }
}
