package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AppColors.ScreenBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.TopBarBlue)
                        .padding(horizontal = AppSpacing.md, vertical = AppSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(end = 92.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
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
                                NexusRfidBrandMark(modifier = Modifier.size(32.dp))
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "Nexus RFID",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = AppColors.TopBarOnBlue
                                )
                                Text(
                                    text = "Menu operacional da sessao",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.TopBarOnBlue.copy(alpha = 0.72f)
                                )
                            }
                        }

                        Text(
                            text = version,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
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

                    Card(
                        shape = AppShapes.card,
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.TopBarOnBlue.copy(alpha = 0.08f)
                        ),
                        border = BorderStroke(1.dp, AppColors.TopBarOnBlue.copy(alpha = 0.08f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(AppSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
                        ) {
                            Text(
                                text = "Fluxos disponiveis",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TopBarOnBlue.copy(alpha = 0.70f)
                            )
                            Text(
                                text = "${primaryItems.size} atalhos prontos para operacao",
                                style = MaterialTheme.typography.titleSmall,
                                color = AppColors.TopBarOnBlue
                            )
                            Text(
                                text = "A identidade visual segue a familia Nexus e preserva a estrutura do legado RFID.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TopBarOnBlue.copy(alpha = 0.72f)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Fluxos da sessao",
                    modifier = Modifier.padding(
                        start = AppSpacing.lg,
                        end = AppSpacing.lg,
                        top = AppSpacing.lg,
                        bottom = AppSpacing.xs
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = AppColors.TextPrimary
                )
            }

            item {
                Card(
                    modifier = Modifier.padding(horizontal = AppSpacing.md),
                    shape = AppShapes.modal,
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
                    border = BorderStroke(1.dp, AppColors.Divider),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(AppSpacing.sm),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                    ) {
                        primaryItems.forEach { item ->
                            DrawerMenuRow(
                                item = item,
                                supportingText = supportTextFor(item.route),
                                selected = item.route == selectedRoute,
                                onClick = { onItemClick(item) }
                            )
                        }
                    }
                }
            }

            if (logoutItem != null) {
                item {
                    Card(
                        modifier = Modifier.padding(AppSpacing.md),
                        shape = AppShapes.card,
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
                        border = BorderStroke(1.dp, AppColors.Divider),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        DrawerMenuRow(
                            item = logoutItem,
                            supportingText = supportTextFor(logoutItem.route),
                            selected = false,
                            onClick = { onItemClick(logoutItem) },
                            exitAction = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerMenuRow(
    item: DrawerMenuItem,
    supportingText: String,
    selected: Boolean,
    onClick: () -> Unit,
    exitAction: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) AppColors.AccentSurface else AppColors.CardSurface,
                shape = AppShapes.card
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    AppColors.BrandSignalBlue.copy(alpha = 0.34f)
                } else {
                    AppColors.Divider
                },
                shape = AppShapes.card
            )
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.md)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = if (selected) 56.dp else if (!exitAction) 44.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        color = if (selected) AppColors.HeroSurface else AppColors.FieldBackground,
                        shape = AppShapes.input
                    )
                    .border(
                        width = 1.dp,
                        color = if (selected) AppColors.BrandSignalBlue.copy(alpha = 0.22f) else AppColors.Divider,
                        shape = AppShapes.input
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconFor(item.route),
                    contentDescription = null,
                    tint = if (exitAction) AppColors.TopBarBlue else AppColors.TextPrimary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected) AppColors.TopBarBlue else AppColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (selected) {
            Text(
                text = "Ativo",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .background(
                        color = AppColors.PrimaryActionBlue.copy(alpha = 0.12f),
                        shape = AppShapes.button
                    )
                    .border(
                        width = 1.dp,
                        color = AppColors.BrandSignalBlue.copy(alpha = 0.24f),
                        shape = AppShapes.button
                    )
                    .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xxs),
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.PrimaryActionBlue
            )
        } else if (!exitAction) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(34.dp)
                    .background(AppColors.FieldBackground, AppShapes.input)
                    .border(1.dp, AppColors.Divider, AppShapes.input),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = AppColors.TextSecondary
                )
            }
        }
    }
}

private fun iconFor(route: String): ImageVector {
    return when (route) {
        "inventory" -> Icons.Outlined.Inventory2
        "products" -> Icons.Outlined.Sell
        "global_search" -> Icons.Outlined.Search
        "associate_tags" -> Icons.Outlined.Sell
        "invoice" -> Icons.Outlined.Description
        "movement" -> Icons.Outlined.SyncAlt
        "settings" -> Icons.Outlined.Settings
        "login" -> Icons.AutoMirrored.Outlined.Logout
        else -> Icons.Outlined.Description
    }
}

private fun supportTextFor(route: String): String {
    return when (route) {
        "inventory" -> "Listas, contagens e leitura ativa"
        "products" -> "Consulta individual de itens"
        "global_search" -> "Busca operacional e rastreio"
        "associate_tags" -> "Associacao de produto com RFID"
        "invoice" -> "Conferencia fiscal e validacao"
        "movement" -> "Fluxo de estoque com envio"
        "settings" -> "Parametros e configuracoes"
        "login" -> "Encerrar sessao atual"
        else -> "Fluxo disponivel nesta sessao"
    }
}
