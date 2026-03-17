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
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
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
            .background(AppColors.ScreenBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    AppColors.BrandSignalBlue.copy(alpha = 0.05f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(horizontal = AppSpacing.lg, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = AppColors.CardSurfaceHighlight,
                                shape = AppShapes.card
                            )
                            .border(
                                width = 1.dp,
                                color = AppColors.BrandSignalBlue.copy(alpha = 0.2f),
                                shape = AppShapes.card
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        NexusRfidBrandMark(modifier = Modifier.size(32.dp))
                    }

                    Column {
                        Text(
                            text = "NEXUS RFID",
                            style = MaterialTheme.typography.labelLarge,
                            color = AppColors.BrandSignalBlue,
                            letterSpacing = 0.2.em
                        )
                        Text(
                            text = version,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
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
                    selected -> AppColors.BrandSignalBlue.copy(alpha = 0.08f)
                    else -> if (nested) Color.Transparent else AppColors.CardSurface
                },
                shape = AppShapes.button
            )
            .border(
                width = 1.dp,
                color = when {
                    selected -> AppColors.BrandSignalBlue.copy(alpha = 0.44f)
                    else -> if (nested) Color.Transparent else AppColors.Divider
                },
                shape = AppShapes.button
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
                tint = if (selected) AppColors.BrandSignalBlue else AppColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = if (nested) item.label else item.label.uppercase(),
                style = if (nested) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelLarge,
                fontSize = if (nested) 13.sp else 11.sp,
                color = if (selected) AppColors.TopBarOnBlue else AppColors.TopBarOnBlue.copy(alpha = 0.7f),
                maxLines = 1,
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
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(16.dp),
                    tint = AppColors.TextSecondary.copy(alpha = 0.4f)
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
