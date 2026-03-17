package com.example.nexusrfid.ui.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nexusrfid.ui.components.DrawerMenu
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.ui.screens.departments.DepartmentsScreen
import com.example.nexusrfid.ui.screens.inventory.InventoryScreen
import com.example.nexusrfid.ui.screens.login.LoginScreen
import com.example.nexusrfid.ui.screens.core.PhasePlaceholderScreen
import com.example.nexusrfid.ui.theme.AppColors
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route,
        modifier = modifier
    ) {
        composable(AppDestination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestination.Departments.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppDestination.Departments.route) {
            DrawerDestination(
                navController = navController,
                currentRoute = AppDestination.Departments.route
            ) { onMenuClick ->
                DepartmentsScreen(
                    departments = MockDataSource.departments,
                    onMenuClick = onMenuClick,
                    onDepartmentClick = {
                        navController.navigate(AppDestination.Inventory.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable(AppDestination.Inventory.route) {
            DrawerDestination(
                navController = navController,
                currentRoute = AppDestination.Inventory.route
            ) { onMenuClick ->
                InventoryScreen(
                    inventories = MockDataSource.inventories,
                    onMenuClick = onMenuClick
                )
            }
        }

        AppDestination.placeholderFlows.forEach { destination ->
            composable(destination.route) {
                PhasePlaceholderScreen(
                    title = destination.title,
                    description = destination.summary,
                    sampleItems = placeholderLinesFor(destination),
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun DrawerDestination(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable (onMenuClick: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.28f),
        drawerContent = {
            Surface(
                modifier = Modifier
                    .width(286.dp)
                    .fillMaxHeight(),
                color = AppColors.CardSurface
            ) {
                DrawerMenu(
                    items = MockDataSource.drawerItems,
                    version = MockDataSource.appVersion,
                    selectedRoute = currentRoute,
                    onItemClick = { item ->
                        coroutineScope.launch {
                            drawerState.close()
                            navigateFromDrawer(
                                navController = navController,
                                currentRoute = currentRoute,
                                targetRoute = item.route
                            )
                        }
                    }
                )
            }
        }
    ) {
        content {
            coroutineScope.launch {
                drawerState.open()
            }
        }
    }
}

private fun navigateFromDrawer(
    navController: NavHostController,
    currentRoute: String,
    targetRoute: String
) {
    when {
        targetRoute == AppDestination.Login.route -> {
            navController.navigate(AppDestination.Login.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        targetRoute != currentRoute -> {
            navController.navigate(targetRoute) {
                launchSingleTop = true
            }
        }
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
