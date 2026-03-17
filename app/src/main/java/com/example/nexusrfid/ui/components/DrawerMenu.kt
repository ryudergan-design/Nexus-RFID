package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.DrawerMenuItem
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun DrawerMenu(
    items: List<DrawerMenuItem>,
    version: String,
    selectedRoute: String?,
    onItemClick: (DrawerMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryItems = items.filterNot { it.route == "login" }
    val logoutItem = items.firstOrNull { it.route == "login" }
    val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AppColors.CardSurface)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.TopBarBlue)
                        .padding(horizontal = AppSpacing.md, vertical = AppSpacing.lg),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    color = AppColors.TopBarOnBlue.copy(alpha = 0.10f),
                                    shape = AppShapes.card
                                )
                                .border(
                                    width = 1.dp,
                                    color = AppColors.TopBarOnBlue.copy(alpha = 0.08f),
                                    shape = AppShapes.card
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            NexusRfidBrandMark(modifier = Modifier.size(28.dp))
                        }

                        Text(
                            text = "Nexus RFID",
                            style = MaterialTheme.typography.titleSmall,
                            color = AppColors.TopBarOnBlue
                        )
                    }

                    Text(
                        text = version,
                        modifier = Modifier
                            .background(
                                color = AppColors.TopBarOnBlue.copy(alpha = 0.10f),
                                shape = AppShapes.button
                            )
                            .border(
                                width = 1.dp,
                                color = AppColors.TopBarOnBlue.copy(alpha = 0.08f),
                                shape = AppShapes.button
                            )
                            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xxs),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TopBarOnBlue
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = AppSpacing.md, vertical = AppSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                ) {
                    primaryItems.forEach { item ->
                        val childSelected = item.children.any { child -> child.route == selectedRoute }
                        val expanded = expandedGroups[item.route] ?: childSelected

                        DrawerMenuRow(
                            item = item,
                            selected = childSelected || item.route == selectedRoute,
                            onClick = {
                                if (item.children.isEmpty()) {
                                    onItemClick(item)
                                } else {
                                    expandedGroups[item.route] = !expanded
                                }
                            },
                            expandable = item.children.isNotEmpty(),
                            expanded = expanded
                        )

                        if (item.children.isNotEmpty() && expanded) {
                            Column(
                                modifier = Modifier.padding(start = AppSpacing.lg, top = AppSpacing.xs),
                                verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                            ) {
                                item.children.forEach { child ->
                                    DrawerMenuRow(
                                        item = child,
                                        selected = child.route == selectedRoute,
                                        onClick = { onItemClick(child) },
                                        nested = true
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (logoutItem != null) {
                item {
                    DrawerMenuRow(
                        item = logoutItem,
                        selected = false,
                        onClick = { onItemClick(logoutItem) },
                        exitAction = true,
                        modifier = Modifier.padding(horizontal = AppSpacing.md, vertical = AppSpacing.md)
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerMenuRow(
    item: DrawerMenuItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    exitAction: Boolean = false,
    expandable: Boolean = false,
    expanded: Boolean = false,
    nested: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = when {
                    selected -> AppColors.AccentSurface
                    nested -> AppColors.FieldBackground
                    else -> AppColors.CardSurface
                },
                shape = AppShapes.card
            )
            .border(
                width = 1.dp,
                color = when {
                    selected -> AppColors.BrandSignalBlue.copy(alpha = 0.34f)
                    nested -> AppColors.AccentBorder
                    else -> AppColors.Divider
                },
                shape = AppShapes.card
            )
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.md)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = if (expandable || !exitAction) 40.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Icon(
                imageVector = iconFor(item.route),
                contentDescription = null,
                tint = if (selected || exitAction) AppColors.TopBarBlue else AppColors.TextSecondary
            )

            Text(
                text = item.label,
                style = if (nested) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium,
                color = if (selected) AppColors.TopBarBlue else AppColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        when {
            expandable -> {
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = AppColors.TextSecondary
                )
            }

            !exitAction -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = AppColors.TextSecondary
                )
            }
        }
    }
}

private fun iconFor(route: String): ImageVector {
    return when (route) {
        "products" -> Icons.Outlined.Sell
        "inventory" -> Icons.Outlined.Inventory2
        "global_search" -> Icons.Outlined.Search
        "conferences" -> Icons.Outlined.Description
        "invoice" -> Icons.Outlined.Description
        "movement" -> Icons.Outlined.SyncAlt
        "settings" -> Icons.Outlined.Settings
        "login" -> Icons.AutoMirrored.Outlined.Logout
        else -> Icons.Outlined.Description
    }
}
