package com.example.nexusrfid.ui.screens.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.data.mock.ProductListItem
import com.example.nexusrfid.ui.components.AppTopBar
import com.example.nexusrfid.ui.components.EmptyStateBox
import com.example.nexusrfid.ui.components.NexusIntroCard
import com.example.nexusrfid.ui.components.SearchHeader
import com.example.nexusrfid.ui.components.SimpleListRow
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

@Composable
fun ProductsScreen(
    products: List<ProductListItem>,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchInput by remember { mutableStateOf("") }
    var activeQuery by remember { mutableStateOf("") }

    val filteredProducts = products.filter { product ->
        activeQuery.isBlank() || product.matchesQuery(activeQuery)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.ScreenBackground,
        topBar = {
            AppTopBar(
                title = "Produtos",
                eyebrow = "Catalogo",
                onNavigationClick = onMenuClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.ScreenBackground)
                .padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            item {
                NexusIntroCard(
                    eyebrow = "Consulta rapida",
                    title = "Pesquise produtos",
                    description = "Digite nome, codigo, reduzido ou EAN para consultar o catalogo.",
                    statLabel = "resultados",
                    statValue = filteredProducts.size.toString()
                )
            }

            item {
                SearchHeader(
                    value = searchInput,
                    onValueChange = { searchInput = it },
                    onSearchClick = { activeQuery = searchInput.trim() },
                    sectionTitle = "Consultar catalogo",
                    placeholder = "Nome, codigo, reduzido ou EAN"
                )
            }

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
                        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                            Text(
                                text = if (activeQuery.isBlank()) "Catalogo completo" else "Resultado da pesquisa",
                                style = MaterialTheme.typography.titleSmall,
                                color = AppColors.TextPrimary
                            )
                            Text(
                                text = if (activeQuery.isBlank()) {
                                    "Lista pronta para consulta."
                                } else {
                                    "Filtro aplicado para \"$activeQuery\"."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TextSecondary
                            )
                        }

                        Text(
                            text = "${filteredProducts.size} itens",
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

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppShapes.card,
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
                    border = BorderStroke(1.dp, AppColors.Divider),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    if (filteredProducts.isEmpty()) {
                        EmptyStateBox(
                            title = "Nenhum produto localizado",
                            supportingText = "Revise a busca e tente novamente.",
                            actionLabel = if (activeQuery.isNotBlank()) "Limpar busca" else null,
                            onAction = if (activeQuery.isNotBlank()) {
                                {
                                    searchInput = ""
                                    activeQuery = ""
                                }
                            } else {
                                null
                            }
                        )
                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            filteredProducts.forEachIndexed { index, product ->
                                SimpleListRow(
                                    title = product.name,
                                    subtitle = "Cod. ${product.code}  |  Red. ${product.reducedCode}",
                                    showDivider = index < filteredProducts.lastIndex
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun ProductListItem.matchesQuery(query: String): Boolean {
    val normalizedQuery = query.normalizeForSearch()
    if (normalizedQuery.isBlank()) return true

    return listOf(name, code, reducedCode, ean13, tagCode)
        .any { field -> field.normalizeForSearch().contains(normalizedQuery) }
}

private fun String.normalizeForSearch(): String {
    return lowercase()
        .replace(" ", "")
        .replace(".", "")
        .replace("-", "")
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun ProductsScreenPreview() {
    NexusRFIDTheme {
        ProductsScreen(
            products = MockDataSource.products,
            onMenuClick = {}
        )
    }
}
