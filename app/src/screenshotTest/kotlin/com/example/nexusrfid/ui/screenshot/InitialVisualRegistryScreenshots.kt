package com.example.nexusrfid.ui.screenshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.ui.components.DrawerMenu
import com.example.nexusrfid.ui.navigation.AppDestination
import com.example.nexusrfid.ui.screens.core.PhasePlaceholderScreen
import com.example.nexusrfid.ui.screens.departments.DepartmentsScreen
import com.example.nexusrfid.ui.screens.inventory.InventoryScreen
import com.example.nexusrfid.ui.screens.login.LoginScreen
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

private const val ScreenWidthDp = 360
private const val ScreenHeightDp = 780

@PreviewTest
@Preview(
    name = "login",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun LoginVisualRegistryScreenshot() {
    NexusRFIDTheme {
        LoginScreen(onLoginSuccess = {})
    }
}

@PreviewTest
@Preview(
    name = "departamentos",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun DepartmentsVisualRegistryScreenshot() {
    NexusRFIDTheme {
        DepartmentsScreen(
            departments = MockDataSource.departments,
            onMenuClick = {},
            onDepartmentClick = {}
        )
    }
}

@PreviewTest
@Preview(
    name = "inventario",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun InventoryVisualRegistryScreenshot() {
    NexusRFIDTheme {
        InventoryScreen(
            inventories = MockDataSource.inventories,
            onMenuClick = {}
        )
    }
}

@PreviewTest
@Preview(
    name = "drawer-departamentos",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun DepartmentsDrawerVisualRegistryScreenshot() {
    DrawerRegistrySurface(selectedRoute = AppDestination.Departments.route)
}

@PreviewTest
@Preview(
    name = "drawer-inventario",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun InventoryDrawerVisualRegistryScreenshot() {
    DrawerRegistrySurface(selectedRoute = AppDestination.Inventory.route)
}

@PreviewTest
@Preview(
    name = "produtos",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun ProductsVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.Products)
}

@PreviewTest
@Preview(
    name = "busca-global",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun GlobalSearchVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.GlobalSearch)
}

@PreviewTest
@Preview(
    name = "associar-tag",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun AssociateTagsVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.AssociateTags)
}

@PreviewTest
@Preview(
    name = "nota-fiscal",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun InvoiceVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.Invoice)
}

@PreviewTest
@Preview(
    name = "movimentacao",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun MovementVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.Movement)
}

@PreviewTest
@Preview(
    name = "configuracoes",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun SettingsVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.Settings)
}

@PreviewTest
@Preview(
    name = "leitura-livre",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun FreeReadVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.FreeRead)
}

@PreviewTest
@Preview(
    name = "dispositivos",
    showBackground = true,
    backgroundColor = 0xFFF2F6FB,
    widthDp = ScreenWidthDp,
    heightDp = ScreenHeightDp
)
@Composable
fun DevicesVisualRegistryScreenshot() {
    PlaceholderRegistryScreen(AppDestination.Devices)
}

@Composable
private fun DrawerRegistrySurface(selectedRoute: String) {
    NexusRFIDTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.ScreenBackground)
        ) {
            DrawerMenu(
                items = MockDataSource.drawerItems,
                version = MockDataSource.appVersion,
                selectedRoute = selectedRoute,
                onItemClick = {},
                modifier = Modifier.width(286.dp)
            )

            Box(
                modifier = Modifier
                    .width(74.dp)
                    .fillMaxHeight()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.14f))
            )
        }
    }
}

@Composable
private fun PlaceholderRegistryScreen(destination: AppDestination) {
    NexusRFIDTheme {
        PhasePlaceholderScreen(
            title = destination.title,
            description = destination.summary,
            sampleItems = placeholderLinesFor(destination),
            onBack = {}
        )
    }
}

private fun placeholderLinesFor(destination: AppDestination): List<String> {
    return when (destination) {
        AppDestination.Products -> MockDataSource.products.take(4).map { "${it.name} - ${it.code}" }
        AppDestination.GlobalSearch -> listOf(
            "Contadores: Lidas e Encontradas",
            "Acoes: iniciar, parar, potencia, som",
            "Tipos: Produto, Reduzido, Ean-13, Tag"
        )

        AppDestination.AssociateTags -> listOf(
            "Mesmo esqueleto da busca de produto",
            "Busca por referencia, cor e tamanho",
            "Feedback de carregamento enxuto"
        )

        AppDestination.Invoice -> listOf(
            "Empty state centralizado",
            "Acao verde para leitura da NF"
        )

        AppDestination.Movement -> listOf(
            "Mesmo componente base da Nota Fiscal",
            "Texto adaptado para movimentacao"
        )

        AppDestination.Settings -> listOf(
            "Secoes: leitor, configuracao e dispositivo padrao",
            "Modal escuro de selecao",
            "Dialogo escuro de permissao"
        )

        AppDestination.FreeRead -> listOf(
            "Controles de leitura no topo",
            "Linhas repetidas de SET/GET",
            "Parametros de RF densos e tecnicos"
        )

        AppDestination.Devices -> listOf(
            "Lista branca simples",
            "Fechar no topo direito",
            "Preparada para descoberta real na Fase 7"
        )

        AppDestination.Login,
        AppDestination.Departments,
        AppDestination.Inventory -> emptyList()
    }
}
