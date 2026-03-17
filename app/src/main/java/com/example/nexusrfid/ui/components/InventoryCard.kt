package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusrfid.data.mock.InventoryCardItem
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun InventoryCard(
    item: InventoryCardItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppShapes.card,
        border = BorderStroke(
            1.dp,
            if (item.selected) AppColors.BrandSignalBlue.copy(alpha = 0.40f) else AppColors.Divider
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (item.selected) AppColors.CardSurfaceHighlight else AppColors.CardSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        if (item.selected) AppColors.BrandSignalBlue else AppColors.Divider
                    )
            )

            Column(
                modifier = Modifier.padding(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InventoryPill(
                        text = if (item.selected) "EM FOCO" else "LISTA ATIVA",
                        containerColor = if (item.selected) {
                            AppColors.BrandSignalBlue.copy(alpha = 0.14f)
                        } else {
                            AppColors.CardSurfaceHighlight
                        },
                        contentColor = if (item.selected) {
                            AppColors.BrandSignalBlue
                        } else {
                            AppColors.TextSecondary
                        }
                    )
                    InventoryPill(
                        text = "${item.systemStock} ITENS",
                        containerColor = Color.Transparent,
                        contentColor = AppColors.TopBarOnBlue,
                        borderColor = if (item.selected) {
                            AppColors.BrandSignalBlue.copy(alpha = 0.44f)
                        } else {
                            AppColors.Divider
                        }
                    )
                }

                Text(
                    text = item.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.TopBarOnBlue,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = AppColors.CardSurfaceHighlight.copy(alpha = 0.2f),
                            shape = AppShapes.input
                        )
                        .border(
                            width = 1.dp,
                            color = AppColors.Divider,
                            shape = AppShapes.input
                        )
                        .padding(AppSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    InventoryMetaRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = AppColors.TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = "ATUALIZADO EM",
                        value = item.dateTime
                    )
                    InventoryMetaRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = AppColors.TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = "RESPONSAVEL",
                        value = item.responsible
                    )
                    InventoryMetaRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Inventory2,
                                contentDescription = null,
                                tint = AppColors.BrandSignalBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = "ESTOQUE DO SISTEMA",
                        value = item.systemStock.toString(),
                        emphasizeValue = true
                    )
                }
            }
        }
    }
}

@Composable
private fun InventoryMetaRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    emphasizeValue: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(AppColors.CardSurfaceHighlight, AppShapes.input)
                .border(0.5.dp, AppColors.Divider, AppShapes.input)
                .padding(AppSpacing.xs),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 9.sp),
                color = AppColors.TextSecondary
            )
            Text(
                text = value,
                style = if (emphasizeValue) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.bodyMedium
                },
                color = if (emphasizeValue) AppColors.BrandSignalBlue else AppColors.TopBarOnBlue,
                fontFamily = if (emphasizeValue) FontFamily.Monospace else FontFamily.SansSerif
            )
        }
    }
}

@Composable
private fun InventoryPill(
    text: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    borderColor: androidx.compose.ui.graphics.Color = containerColor
) {
    Box(
        modifier = Modifier
            .background(containerColor, AppShapes.button)
            .border(1.dp, borderColor, AppShapes.button)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xxs),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor
        )
    }
}
