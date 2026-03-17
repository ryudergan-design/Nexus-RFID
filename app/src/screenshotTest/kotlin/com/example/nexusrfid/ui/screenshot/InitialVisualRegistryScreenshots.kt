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
import com.example.nexusrfid.ui.screens.globalsearch.GlobalSearchScreen
import com.example.nexusrfid.ui.screens.core.PhasePlaceholderScreen
import com.example.nexusrfid.ui.screens.departments.DepartmentsScreen
import com.example.nexusrfid.ui.screens.inventory.InventoryScreen
import com.example.nexusrfid.ui.screens.login.LoginScreen
import com.example.nexusrfid.ui.screens.products.ProductsScreen
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
    NexusRFIDTheme {
        ProductsScreen(
            products = MockDataSource.products,
            onMenuClick = {}
        )
    }
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
    NexusRFIDTheme {
        GlobalSearchScreen(
            products = MockDataSource.products,
            searchSummary = MockDataSource.searchSummary,
            searchTargets = MockDataSource.searchTargets,
            searchTypes = MockDataSource.searchTypes,
            onMenuClick = {}
        )
    }
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
        AppDestination.AssociateTags -> listOf(
            "Selecione o produto desejado",
            "Associe a etiqueta ao item",
            "Continue com a leitura depois do vinculo"
        )

        AppDestination.Invoice -> listOf(
            "Leia a nota fiscal",
            "Confira os itens recebidos"
        )

        AppDestination.Movement -> listOf(
            "Confira entrada e saida",
            "Visual semelhante ao da nota fiscal"
        )

        AppDestination.Settings -> listOf(
            "Escolha o leitor",
            "Defina o dispositivo padrao",
            "Ajuste preferencias da operacao"
        )

        AppDestination.FreeRead -> listOf(
            "Acompanhe as leituras em tempo real",
            "Ajuste os principais comandos",
            "Veja os dados durante a leitura"
        )

        AppDestination.Devices -> listOf(
            "Veja os dispositivos disponiveis",
            "Escolha com qual deseja conectar",
            "Fluxo pronto para a integracao futura"
        )

        AppDestination.Login,
        AppDestination.Departments,
        AppDestination.Inventory,
        AppDestination.Products,
        AppDestination.GlobalSearch -> emptyList()
    }
}
