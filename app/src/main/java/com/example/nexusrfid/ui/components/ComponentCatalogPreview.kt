package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

@Composable
private fun ComponentCatalog() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        AppTopBar(title = "Catalogo Base", onNavigationClick = {})

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)
        ) {
            Text(
                text = "Marca",
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TextPrimary
            )
            NexusRfidBrandLockup()

            Text(
                text = "Botoes",
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TextPrimary
            )
            ActionButtonPrimary(text = "Entrar", onClick = {})
            ActionButtonOutline(
                text = "Adicionar Targets",
                onClick = {},
                borderColor = AppColors.PositiveBorder,
                contentColor = AppColors.PositiveGreen
            )

            Text(
                text = "Linhas e card",
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TextPrimary
            )
            SimpleListRow(
                title = "39952 - NOVOS HORIZONTES MODA",
                subtitle = "Leitura densa com divisor discreto"
            )
            InventoryCard(item = MockDataSource.inventories.first())

            Text(
                text = "Estado vazio",
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TextPrimary
            )
            EmptyStateBox(
                title = "Nenhuma nota fiscal identificada",
                actionLabel = "Ler codigo de barras da NF",
                onAction = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 820)
@Composable
fun ComponentCatalogPreview() {
    NexusRFIDTheme {
        ComponentCatalog()
    }
}
