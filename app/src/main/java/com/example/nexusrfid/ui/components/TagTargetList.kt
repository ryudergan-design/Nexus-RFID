package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

enum class TagTargetKind {
    Tag,
    Reduced,
    Ean13,
    Product
}

data class TagTargetItemUi(
    val epc: String,
    val proximityPercent: Int,
    val proximityLabel: String,
    val matchedProductName: String? = null,
    val lastSeenAtMillis: Long? = null,
    val kind: TagTargetKind = TagTargetKind.Tag
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
        colors = CardDefaults.cardColors(containerColor = AppColors.AccentSurface),
        border = BorderStroke(1.dp, AppColors.AccentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                    Text(
                        text = "TAGS ALVO",
                        style = MaterialTheme.typography.labelLarge,
                        color = AppColors.TextSecondary
                    )
                    Text(
                        text = "${items.size} pronta(s) para busca",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppColors.TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(AppColors.TopBarBlue, CircleShape)
                        .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs)
                ) {
                    Text(
                        text = items.size.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = AppColors.TopBarOnBlue
                    )
                }
            }

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
                .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(AppColors.TopBarBlue.copy(alpha = 0.08f), CircleShape)
                    .padding(AppSpacing.sm),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Sell,
                    contentDescription = null,
                    tint = AppColors.TopBarBlue
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                Text(
                    text = when (item.kind) {
                        TagTargetKind.Tag -> "EPC"
                        TagTargetKind.Reduced -> "REDUZIDO"
                        TagTargetKind.Ean13 -> "EAN-13"
                        TagTargetKind.Product -> "PRODUTO"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = AppColors.TextSecondary
                )
                Text(
                    text = item.epc,
                    style = MaterialTheme.typography.titleSmall.copy(fontFamily = FontFamily.Monospace),
                    color = AppColors.TextPrimary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                proximityColorFor(item.proximityPercent).copy(alpha = 0.14f),
                                CircleShape
                            )
                            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xxs)
                    ) {
                        Text(
                            text = "${item.proximityPercent}% ${item.proximityLabel}",
                            style = MaterialTheme.typography.labelLarge,
                            color = proximityColorFor(item.proximityPercent)
                        )
                    }

                    if (!item.matchedProductName.isNullOrBlank()) {
                        Text(
                            text = item.matchedProductName,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )
                    }
                }
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
