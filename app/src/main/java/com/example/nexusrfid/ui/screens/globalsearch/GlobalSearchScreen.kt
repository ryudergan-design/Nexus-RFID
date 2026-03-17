package com.example.nexusrfid.ui.screens.globalsearch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.data.mock.ProductListItem
import com.example.nexusrfid.data.mock.ProductTargetState
import com.example.nexusrfid.data.mock.SearchCounterSummary
import com.example.nexusrfid.data.mock.SearchTargetItem
import com.example.nexusrfid.data.mock.SearchTypeOption
import com.example.nexusrfid.ui.components.AppDialog
import com.example.nexusrfid.ui.components.AppTopBar
import com.example.nexusrfid.ui.components.CounterBar
import com.example.nexusrfid.ui.components.EmptyStateBox
import com.example.nexusrfid.ui.components.SearchHeader
import com.example.nexusrfid.ui.components.SearchTypeSheet
import com.example.nexusrfid.ui.components.SimpleListRow
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

private const val ProductSearchTypeKey = "product"

@Composable
fun GlobalSearchScreen(
    products: List<ProductListItem>,
    searchSummary: SearchCounterSummary,
    searchTargets: List<SearchTargetItem>,
    searchTypes: List<SearchTypeOption>,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialSelectedTypeKey: String = ProductSearchTypeKey,
    initialSelectedTargetKey: String = "all",
    initialSearchValue: String = "",
    initialSheetOpen: Boolean = false,
    initialDialogTypeKey: String? = null
) {
    val fallbackType = searchTypes.first()
    val initialType = searchTypes.firstOrNull { it.key == initialSelectedTypeKey } ?: fallbackType

    var productInput by remember(initialSelectedTypeKey, initialSearchValue) {
        mutableStateOf(if (initialType.key == ProductSearchTypeKey) initialSearchValue else "")
    }
    var searchValue by remember(initialSearchValue) { mutableStateOf(initialSearchValue) }
    var selectedType by remember(initialSelectedTypeKey) { mutableStateOf(initialType) }
    var selectedTargetKey by remember(initialSelectedTargetKey) { mutableStateOf(initialSelectedTargetKey) }
    var readingActive by remember { mutableStateOf(false) }
    var showTypeSheet by remember(initialSheetOpen) { mutableStateOf(initialSheetOpen) }
    var dialogOption by remember(initialDialogTypeKey) {
        mutableStateOf(searchTypes.firstOrNull { it.key == initialDialogTypeKey })
    }
    var dialogValue by remember(initialSearchValue) { mutableStateOf(initialSearchValue) }

    val defaultTargetKey = searchTargets.first().key
    val selectedTarget = searchTargets.firstOrNull { it.key == selectedTargetKey } ?: searchTargets.first()
    val filteredProducts = products
        .filter { product ->
            searchValue.isNotBlank() &&
                matchesSearchType(product = product, searchTypeKey = selectedType.key, value = searchValue)
        }
        .filter { product ->
            selectedTarget.targetState == null || product.targetState == selectedTarget.targetState
        }

    fun openDialogFor(option: SearchTypeOption) {
        dialogOption = option
        dialogValue = if (selectedType.key == option.key) searchValue else ""
    }

    fun selectSearchType(option: SearchTypeOption) {
        selectedType = option
        showTypeSheet = false

        if (option.requiresManualEntry) {
            productInput = ""
            searchValue = ""
            openDialogFor(option)
        } else {
            searchValue = ""
            productInput = ""
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.ScreenBackground)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.ScreenBackground,
        topBar = {
            AppTopBar(
                title = "Buscar Produtos",
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
                    SearchActionRow(
                        currentType = selectedType.label,
                        currentValue = searchValue,
                        readingActive = readingActive,
                        onStartReading = { readingActive = true },
                        onStopReading = { readingActive = false },
                        onOpenType = { showTypeSheet = true },
                        onClear = {
                            readingActive = false
                            productInput = ""
                            searchValue = ""
                            selectedTargetKey = defaultTargetKey
                        }
                    )
                }

                item {
                    CounterBar(
                        readCount = searchSummary.readCount,
                        foundCount = searchSummary.foundCount
                    )
                }

                item {
                    TargetSelector(
                        targets = searchTargets,
                        products = products,
                        selectedKey = selectedTargetKey,
                        onSelect = { selectedTargetKey = it }
                    )
                }

                if (selectedType.key == ProductSearchTypeKey) {
                    item {
                        SearchHeader(
                            value = productInput,
                            onValueChange = { productInput = it },
                            onSearchClick = { searchValue = productInput.trim() },
                            placeholder = "Nome, codigo, reduzido ou EAN"
                        )
                    }
                }

                item {
                    when {
                        selectedType.requiresManualEntry && searchValue.isBlank() -> {
                            EmptyStateBox(
                                title = "Informe o valor da busca",
                                supportingText = "Use ${selectedType.label} para localizar os produtos.",
                                actionLabel = "Informar ${selectedType.label}",
                                onAction = { openDialogFor(selectedType) }
                            )
                        }

                        selectedType.key == ProductSearchTypeKey && searchValue.isBlank() -> {
                            EmptyStateBox(
                                title = "Digite um produto para buscar",
                                supportingText = "Informe nome ou codigo e toque em Buscar."
                            )
                        }

                        filteredProducts.isEmpty() -> {
                            EmptyStateBox(
                                title = "Nenhum produto localizado",
                                supportingText = "Tente outro valor ou troque o tipo de busca.",
                                actionLabel = "Escolher outro tipo",
                                onAction = { showTypeSheet = true }
                            )
                        }

                        else -> {
                            SearchResultsCard(
                                products = filteredProducts,
                                selectedTarget = selectedTarget.label
                            )
                        }
                    }
                }
            }
        }

        if (showTypeSheet) {
            SearchTypeSheet(
                options = searchTypes,
                selectedKey = selectedType.key,
                onSelect = ::selectSearchType,
                onDismiss = { showTypeSheet = false }
            )
        }
    }

    dialogOption?.let { option ->
        AppDialog(
            title = option.dialogTitle ?: option.label,
            value = dialogValue,
            onValueChange = { dialogValue = it },
            placeholder = option.inputPlaceholder ?: "Informe o valor",
            numericInput = option.numericInput,
            onDismiss = { dialogOption = null },
            onConfirm = {
                selectedType = option
                searchValue = dialogValue.trim()
                dialogOption = null
            }
        )
    }
}

@Composable
private fun SearchActionRow(
    currentType: String,
    currentValue: String,
    readingActive: Boolean,
    onStartReading: () -> Unit,
    onStopReading: () -> Unit,
    onOpenType: () -> Unit,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
        border = BorderStroke(1.dp, AppColors.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val itemWidth = (maxWidth - (AppSpacing.sm * 3)) / 4

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    ActionCell(
                        label = "Ler",
                        active = readingActive,
                        modifier = Modifier.width(itemWidth),
                        onClick = onStartReading
                    )
                    ActionCell(
                        label = "Parar",
                        modifier = Modifier.width(itemWidth),
                        onClick = onStopReading
                    )
                    ActionCell(
                        label = "Tipo",
                        modifier = Modifier.width(itemWidth),
                        onClick = onOpenType
                    )
                    ActionCell(
                        label = "Limpar",
                        modifier = Modifier.width(itemWidth),
                        onClick = onClear
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentType,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )

                if (currentValue.isNotBlank()) {
                    Text(
                        text = currentValue,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TopBarBlue
                    )
                } else {
                    Text(
                        text = if (readingActive) "Lendo" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TopBarBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionCell(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = AppShapes.button,
        border = BorderStroke(
            width = 1.dp,
            color = if (active) AppColors.BrandSignalBlue.copy(alpha = 0.44f) else AppColors.Divider
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (active) AppColors.AccentSurface else AppColors.FieldBackground,
            contentColor = if (active) AppColors.TopBarBlue else AppColors.TextPrimary
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TargetSelector(
    targets: List<SearchTargetItem>,
    products: List<ProductListItem>,
    selectedKey: String,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
        border = BorderStroke(1.dp, AppColors.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                targets.forEach { target ->
                    val count = if (target.targetState == null) {
                        products.size
                    } else {
                        products.count { it.targetState == target.targetState }
                    }

                    TargetButton(
                        label = target.label,
                        count = count,
                        selected = target.key == selectedKey,
                        onClick = { onSelect(target.key) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TargetButton(
    label: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = AppShapes.button,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) AppColors.BrandSignalBlue.copy(alpha = 0.34f) else AppColors.Divider
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) AppColors.AccentSurface else AppColors.FieldBackground,
            contentColor = if (selected) AppColors.TopBarBlue else AppColors.TextPrimary
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(AppColors.CardSurface, AppShapes.button)
                    .border(1.dp, AppColors.Divider, AppShapes.button),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun SearchResultsCard(
    products: List<ProductListItem>,
    selectedTarget: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
        border = BorderStroke(1.dp, AppColors.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            products.forEachIndexed { index, product ->
                SimpleListRow(
                    title = product.name,
                    subtitle = "Cod. ${product.code}  |  $selectedTarget  |  ${targetStateLabel(product.targetState)}",
                    showDivider = index < products.lastIndex
                )
            }
        }
    }
}

private fun matchesSearchType(
    product: ProductListItem,
    searchTypeKey: String,
    value: String
): Boolean {
    val normalizedValue = value.normalizeForSearch()
    if (normalizedValue.isBlank()) return false

    val source = when (searchTypeKey) {
        "product" -> listOf(product.name, product.code, product.reducedCode, product.ean13)
        "reduced" -> listOf(product.reducedCode)
        "ean13" -> listOf(product.ean13)
        "tag" -> listOf(product.tagCode)
        else -> emptyList()
    }

    return source.any { it.normalizeForSearch().contains(normalizedValue) }
}

private fun targetStateLabel(state: ProductTargetState): String {
    return when (state) {
        ProductTargetState.Pending -> "Pendente"
        ProductTargetState.Found -> "Encontrado"
        ProductTargetState.Divergent -> "Divergente"
    }
}

private fun String.normalizeForSearch(): String {
    return lowercase()
        .replace(" ", "")
        .replace(".", "")
        .replace("-", "")
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun GlobalSearchScreenPreview() {
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

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun GlobalSearchScreenSheetPreview() {
    NexusRFIDTheme {
        GlobalSearchScreen(
            products = MockDataSource.products,
            searchSummary = MockDataSource.searchSummary,
            searchTargets = MockDataSource.searchTargets,
            searchTypes = MockDataSource.searchTypes,
            onMenuClick = {},
            initialSheetOpen = true
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun GlobalSearchScreenDialogPreview() {
    NexusRFIDTheme {
        GlobalSearchScreen(
            products = MockDataSource.products,
            searchSummary = MockDataSource.searchSummary,
            searchTargets = MockDataSource.searchTargets,
            searchTypes = MockDataSource.searchTypes,
            onMenuClick = {},
            initialSelectedTypeKey = "reduced",
            initialDialogTypeKey = "reduced"
        )
    }
}
