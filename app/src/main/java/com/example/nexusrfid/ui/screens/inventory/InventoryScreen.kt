package com.example.nexusrfid.ui.screens.inventory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.InventoryCardItem
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.ui.components.AppTopBar
import com.example.nexusrfid.ui.components.InventoryCard
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

@Composable
fun InventoryScreen(
    inventories: List<InventoryCardItem>,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val latestUpdate = inventories.firstOrNull()?.dateTime ?: "-"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.ScreenBackground,
        topBar = {
            AppTopBar(
                title = "Inventario",
                eyebrow = null,
                onNavigationClick = onMenuClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.ScreenBackground)
                .padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppShapes.card,
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
                    border = BorderStroke(1.dp, AppColors.Divider),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppSpacing.lg),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        androidx.compose.foundation.layout.Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Inventarios",
                                style = MaterialTheme.typography.titleSmall,
                                color = AppColors.TextPrimary
                            )
                            Text(
                                text = "Atualizado com base em $latestUpdate",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TextSecondary
                            )
                        }

                        Text(
                            text = "${inventories.size} listas",
                            modifier = Modifier
                                .background(AppColors.FieldBackground, AppShapes.button)
                                .border(1.dp, AppColors.Divider, AppShapes.button)
                                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xxs),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TopBarBlue
                        )
                    }
                }
            }

            items(inventories) { inventory ->
                InventoryCard(item = inventory)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun InventoryScreenPreview() {
    NexusRFIDTheme {
        InventoryScreen(
            inventories = MockDataSource.inventories,
            onMenuClick = {}
        )
    }
}
