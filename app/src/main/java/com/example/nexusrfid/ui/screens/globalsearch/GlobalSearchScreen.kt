package com.example.nexusrfid.ui.screens.globalsearch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.data.mock.ProductListItem
import com.example.nexusrfid.data.mock.ProductTargetState
import com.example.nexusrfid.data.mock.RfidTargetPreviewItem
import com.example.nexusrfid.data.mock.SearchCounterSummary
import com.example.nexusrfid.data.mock.SearchTargetItem
import com.example.nexusrfid.data.mock.SearchTypeOption
import com.example.nexusrfid.rfid.CollectorModel
import com.example.nexusrfid.ui.app.NexusRfidAppState
import com.example.nexusrfid.ui.components.ActionButtonOutline
import com.example.nexusrfid.ui.components.ActionButtonPrimary
import com.example.nexusrfid.ui.components.AppDialog
import com.example.nexusrfid.ui.components.AppTopBar
import com.example.nexusrfid.ui.components.CounterBar
import com.example.nexusrfid.ui.components.EmptyStateBox
import com.example.nexusrfid.ui.components.ProximityIndicator
import com.example.nexusrfid.ui.components.SearchHeader
import com.example.nexusrfid.ui.components.SearchTypeSheet
import com.example.nexusrfid.ui.components.SimpleListRow
import com.example.nexusrfid.ui.components.TagTargetItemUi
import com.example.nexusrfid.ui.components.TagTargetList
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ProductSearchTypeKey = "product"
private const val TagSearchTypeKey = "tag"

@Composable
fun GlobalSearchScreen(
    products: List<ProductListItem>,
    searchSummary: SearchCounterSummary,
    searchTargets: List<SearchTargetItem>,
    searchTypes: List<SearchTypeOption>,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    appState: NexusRfidAppState? = null,
    initialSelectedTypeKey: String = ProductSearchTypeKey,
    initialSelectedTargetKey: String = "all",
    initialSearchValue: String = "",
    initialSheetOpen: Boolean = false,
    initialDialogTypeKey: String? = null,
    initialTagTargets: List<RfidTargetPreviewItem> = emptyList()
) {
    val coroutineScope = rememberCoroutineScope()
    val fallbackType = searchTypes.first()
    val initialType = searchTypes.firstOrNull { it.key == initialSelectedTypeKey } ?: fallbackType

    var productInput by remember(initialSelectedTypeKey, initialSearchValue) {
        mutableStateOf(if (initialType.key == ProductSearchTypeKey) initialSearchValue else "")
    }
    var searchValue by remember(initialSearchValue) { mutableStateOf(initialSearchValue) }
    var selectedType by remember(initialSelectedTypeKey) { mutableStateOf(initialType) }
    var selectedTargetKey by remember(initialSelectedTargetKey) { mutableStateOf(initialSelectedTargetKey) }
    var showTypeSheet by remember(initialSheetOpen) { mutableStateOf(initialSheetOpen) }
    var dialogOption by remember(initialDialogTypeKey) {
        mutableStateOf(searchTypes.firstOrNull { it.key == initialDialogTypeKey })
    }
    var dialogValue by remember(initialSearchValue) { mutableStateOf(initialSearchValue) }
    var dialogErrorMessage by remember { mutableStateOf<String?>(null) }
    var rfidSearching by remember { mutableStateOf(false) }
    var readCount by remember { mutableIntStateOf(0) }
    var foundCount by remember { mutableIntStateOf(0) }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    var tagTargets by remember(initialTagTargets) {
        mutableStateOf(initialTagTargets.map { it.toUiModel() })
    }

    val defaultTargetKey = searchTargets.first().key
    val selectedTarget = searchTargets.firstOrNull { it.key == selectedTargetKey } ?: searchTargets.first()
    val isTagMode = selectedType.key == TagSearchTypeKey

    val filteredProducts = products
        .filter { product ->
            searchValue.isNotBlank() &&
                matchesSearchType(product = product, searchTypeKey = selectedType.key, value = searchValue)
        }
        .filter { product ->
            selectedTarget.targetState == null || product.targetState == selectedTarget.targetState
        }

    val matchedProducts = tagTargets
        .mapNotNull { target ->
            products.firstOrNull { it.tagCode.normalizedTag() == target.epc }
        }
        .distinctBy { it.code }
        .filter { product ->
            selectedTarget.targetState == null || product.targetState == selectedTarget.targetState
        }

    val counterReadCount = if (isTagMode) readCount else searchSummary.readCount
    val counterFoundCount = if (isTagMode) foundCount else searchSummary.foundCount
    val leadingTarget = tagTargets.maxByOrNull { it.proximityPercent }

    fun stopRfidSearch(resetFeedback: Boolean) {
        searchJob?.cancel()
        searchJob = null
        if (rfidSearching) {
            appState?.stopInventory()
        }
        rfidSearching = false

        if (resetFeedback) {
            tagTargets = tagTargets.map {
                it.copy(
                    proximityPercent = 0,
                    proximityLabel = "Aguardando",
                    lastSeenAtMillis = null
                )
            }
            readCount = 0
            foundCount = 0
        }
    }

    fun openDialogFor(option: SearchTypeOption) {
        dialogOption = option
        dialogErrorMessage = null
        dialogValue = if (selectedType.key == option.key) searchValue else ""
    }

    fun selectSearchType(option: SearchTypeOption) {
        stopRfidSearch(resetFeedback = false)
        selectedType = option
        showTypeSheet = false
        if (option.key == TagSearchTypeKey) {
            productInput = ""
            searchValue = ""
            dialogValue = ""
            dialogErrorMessage = null
        } else if (option.requiresManualEntry) {
            productInput = ""
            searchValue = ""
            openDialogFor(option)
        } else {
            productInput = ""
            searchValue = ""
        }
    }

    fun clearAll() {
        stopRfidSearch(resetFeedback = true)
        productInput = ""
        searchValue = ""
        selectedTargetKey = defaultTargetKey
        tagTargets = emptyList()
        dialogValue = ""
        dialogErrorMessage = null
        appState?.clearErrorMessage()
    }

    fun addTagTarget(rawValue: String) {
        val normalizedTag = rawValue.normalizedTag()
        when {
            normalizedTag.length != 24 -> dialogErrorMessage = "A tag precisa ter 24 caracteres."
            tagTargets.any { it.epc == normalizedTag } -> dialogErrorMessage = "Essa tag ja foi adicionada."
            else -> {
                tagTargets = tagTargets + TagTargetItemUi(
                    epc = normalizedTag,
                    proximityPercent = 0,
                    proximityLabel = "Aguardando"
                )
                dialogValue = ""
                dialogErrorMessage = null
                dialogOption = null
            }
        }
    }

    fun refreshFoundCount() {
        foundCount = tagTargets.count { it.proximityPercent > 0 }
    }

    fun updateTargetsFromReads() {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            while (isActive && rfidSearching) {
                val batch = withContext(Dispatchers.IO) { appState?.readTagBatch().orEmpty() }
                val now = System.currentTimeMillis()
                if (batch.isNotEmpty()) {
                    readCount += batch.size
                    var matchFoundInCycle = false
                    val updatedTargets = tagTargets.associateBy { it.epc }.toMutableMap()
                    batch.forEach { tagRead ->
                        val normalizedEpc = tagRead.epc.normalizedTag()
                        val existingTarget = updatedTargets[normalizedEpc] ?: return@forEach
                        val percent = rssiToPercent(tagRead.rssi)
                        val matchedProduct = products.firstOrNull { it.tagCode.normalizedTag() == normalizedEpc }
                        if (percent > 0) {
                            matchFoundInCycle = true
                            updatedTargets[normalizedEpc] = existingTarget.copy(
                                proximityPercent = maxOf(existingTarget.proximityPercent, percent),
                                proximityLabel = proximityLabelFor(percent),
                                matchedProductName = matchedProduct?.name ?: existingTarget.matchedProductName,
                                lastSeenAtMillis = now
                            )
                        }
                    }
                    tagTargets = tagTargets.map { updatedTargets[it.epc] ?: it }
                    if (matchFoundInCycle) {
                        appState?.playDetectionTone()
                    }
                }
                tagTargets = tagTargets.map { target ->
                    val seenAt = target.lastSeenAtMillis ?: return@map target
                    if (now - seenAt > 1200L) {
                        target.copy(
                            proximityPercent = 0,
                            proximityLabel = if (rfidSearching) "Aguardando" else "Pronto",
                            lastSeenAtMillis = null
                        )
                    } else {
                        target
                    }
                }
                refreshFoundCount()
                delay(180)
            }
        }
    }

    fun startSearch() {
        appState?.clearErrorMessage()
        if (!isTagMode) {
            searchValue = productInput.trim()
            return
        }
        dialogErrorMessage = null
        if (tagTargets.isEmpty()) {
            dialogErrorMessage = "Adicione pelo menos uma tag para iniciar."
            return
        }
        if (appState?.selectedCollectorModel == CollectorModel.C72) {
            dialogErrorMessage = "O primeiro teste real desta fase esta habilitado apenas para o R6."
            return
        }
        if (appState != null && !appState.startInventory()) {
            return
        }
        stopRfidSearch(resetFeedback = true)
        rfidSearching = true
        updateTargetsFromReads()
    }

    DisposableEffect(Unit) {
        onDispose { stopRfidSearch(resetFeedback = false) }
    }

    GlobalSearchContent(
        products = products,
        searchTargets = searchTargets,
        searchTypes = searchTypes,
        selectedTarget = selectedTarget,
        selectedTargetKey = selectedTargetKey,
        selectedType = selectedType,
        isTagMode = isTagMode,
        onSelectTarget = { selectedTargetKey = it },
        onMenuClick = onMenuClick,
        onOpenType = { showTypeSheet = true },
        onStartSearch = ::startSearch,
        onStopSearch = { stopRfidSearch(resetFeedback = false) },
        onClear = ::clearAll,
        onAddTag = { openDialogFor(searchTypes.first { it.key == TagSearchTypeKey }) },
        onClearTags = {
            stopRfidSearch(resetFeedback = true)
            tagTargets = emptyList()
        },
        onRemoveTag = { tagToRemove ->
            tagTargets = tagTargets.filterNot { it.epc == tagToRemove }
            refreshFoundCount()
        },
        onDecreasePower = {
            appState?.let { state ->
                state.updateReaderPower((state.readerPower - 1).coerceAtLeast(1))
            }
        },
        onIncreasePower = {
            appState?.let { state ->
                state.updateReaderPower((state.readerPower + 1).coerceAtMost(30))
            }
        },
        collectorLabel = appState?.selectedCollectorModel?.label ?: "R6",
        collectorStatus = appState?.statusMessage ?: "Fluxo visual da busca RFID.",
        currentValue = if (isTagMode && tagTargets.isNotEmpty()) {
            "${tagTargets.size} tag(s) adicionada(s)"
        } else {
            searchValue
        },
        readingActive = rfidSearching,
        powerLevel = appState?.readerPower ?: 15,
        readCount = counterReadCount,
        foundCount = counterFoundCount,
        searchInput = productInput,
        onSearchInputChange = { productInput = it },
        onTextSearch = { searchValue = productInput.trim() },
        filteredProducts = filteredProducts,
        matchedProducts = matchedProducts,
        tagTargets = tagTargets,
        leadingTarget = leadingTarget,
        errorMessage = appState?.errorMessage ?: dialogErrorMessage,
        modifier = modifier
    )

    if (showTypeSheet) {
        SearchTypeSheet(
            options = searchTypes,
            selectedKey = selectedType.key,
            onSelect = ::selectSearchType,
            onDismiss = { showTypeSheet = false }
        )
    }

    dialogOption?.let { option ->
        AppDialog(
            title = if (option.key == TagSearchTypeKey) "Adicionar tag" else option.dialogTitle ?: option.label,
            value = dialogValue,
            onValueChange = { dialogValue = it.uppercase() },
            placeholder = option.inputPlaceholder ?: "Informe o valor",
            numericInput = option.numericInput,
            onDismiss = {
                dialogOption = null
                dialogErrorMessage = null
            },
            onConfirm = {
                if (option.key == TagSearchTypeKey) {
                    addTagTarget(dialogValue)
                } else {
                    selectedType = option
                    searchValue = dialogValue.trim()
                    dialogOption = null
                }
            },
            confirmEnabled = when (option.key) {
                TagSearchTypeKey -> dialogValue.normalizedTag().length == 24
                else -> dialogValue.isNotBlank()
            }
        )
    }
}

@Composable
private fun GlobalSearchContent(
    products: List<ProductListItem>,
    searchTargets: List<SearchTargetItem>,
    searchTypes: List<SearchTypeOption>,
    selectedTarget: SearchTargetItem,
    selectedTargetKey: String,
    selectedType: SearchTypeOption,
    isTagMode: Boolean,
    onSelectTarget: (String) -> Unit,
    onMenuClick: () -> Unit,
    onOpenType: () -> Unit,
    onStartSearch: () -> Unit,
    onStopSearch: () -> Unit,
    onClear: () -> Unit,
    onAddTag: () -> Unit,
    onClearTags: () -> Unit,
    onRemoveTag: (String) -> Unit,
    onDecreasePower: () -> Unit,
    onIncreasePower: () -> Unit,
    collectorLabel: String,
    collectorStatus: String,
    currentValue: String,
    readingActive: Boolean,
    powerLevel: Int,
    readCount: Int,
    foundCount: Int,
    searchInput: String,
    onSearchInputChange: (String) -> Unit,
    onTextSearch: () -> Unit,
    filteredProducts: List<ProductListItem>,
    matchedProducts: List<ProductListItem>,
    tagTargets: List<TagTargetItemUi>,
    leadingTarget: TagTargetItemUi?,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val proximityTitle = if (leadingTarget != null && leadingTarget.proximityPercent > 0) {
        "Tag mais proxima"
    } else {
        "Sinal RFID"
    }
    val proximityPercent = leadingTarget?.proximityPercent ?: 0
    val proximityLabel = leadingTarget?.proximityLabel ?: if (readingActive) "Aguardando" else "Pronto"
    val proximitySupportingText = when {
        leadingTarget != null && leadingTarget.proximityPercent > 0 -> {
            buildString {
                append(leadingTarget.epc)
                if (!leadingTarget.matchedProductName.isNullOrBlank()) {
                    append("  |  ")
                    append(leadingTarget.matchedProductName)
                }
            }
        }

        tagTargets.isEmpty() -> "Adicione as tags alvo para iniciar a localizacao."
        readingActive -> "Aproxime o coletor da area onde a tag pode estar."
        else -> "Conecte o coletor e toque em Iniciar para acompanhar a proximidade."
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
                        currentValue = currentValue,
                        readingActive = readingActive,
                        collectorLabel = collectorLabel,
                        collectorStatus = collectorStatus,
                        showPowerControl = isTagMode,
                        powerLevel = powerLevel,
                        onStartReading = onStartSearch,
                        onStopReading = onStopSearch,
                        onOpenType = onOpenType,
                        onClear = onClear,
                        onDecreasePower = onDecreasePower,
                        onIncreasePower = onIncreasePower
                    )
                }

                item {
                    CounterBar(
                        readCount = readCount,
                        foundCount = foundCount
                    )
                }

                item {
                    TargetSelector(
                        targets = searchTargets,
                        products = products,
                        selectedKey = selectedTargetKey,
                        onSelect = onSelectTarget
                    )
                }

                if (isTagMode) {
                    item {
                        TagCommandCard(
                            tagCount = tagTargets.size,
                            onAddTag = onAddTag,
                            onClearTags = onClearTags
                        )
                    }

                    if (tagTargets.isNotEmpty()) {
                        item {
                            TagTargetList(
                                items = tagTargets,
                                onRemove = onRemoveTag
                            )
                        }
                    }

                    item {
                        ProximityIndicator(
                            title = proximityTitle,
                            percent = proximityPercent,
                            label = proximityLabel,
                            supportingText = proximitySupportingText
                        )
                    }
                } else if (selectedType.key == ProductSearchTypeKey) {
                    item {
                        SearchHeader(
                            value = searchInput,
                            onValueChange = onSearchInputChange,
                            onSearchClick = onTextSearch,
                            placeholder = "Nome, codigo, reduzido ou EAN"
                        )
                    }
                }

                if (!errorMessage.isNullOrBlank()) {
                    item {
                        InlineNoticeCard(message = errorMessage)
                    }
                }

                item {
                    when {
                        isTagMode && tagTargets.isEmpty() -> {
                            EmptyStateBox(
                                title = "Adicione a primeira tag",
                                supportingText = "Digite o EPC e monte a lista de alvos para buscar.",
                                actionLabel = "Adicionar tag",
                                onAction = onAddTag
                            )
                        }

                        isTagMode && matchedProducts.isEmpty() -> {
                            EmptyStateBox(
                                title = "Nenhum produto vinculado a essas tags",
                                supportingText = "A busca RFID continua funcionando mesmo sem dados externos."
                            )
                        }

                        isTagMode -> {
                            SearchResultsCard(
                                products = matchedProducts,
                                selectedTarget = selectedTarget.label
                            )
                        }

                        selectedType.requiresManualEntry && currentValue.isBlank() -> {
                            EmptyStateBox(
                                title = "Informe o valor da busca",
                                supportingText = "Use ${selectedType.label} para localizar os produtos.",
                                actionLabel = "Informar ${selectedType.label}",
                                onAction = onOpenType
                            )
                        }

                        selectedType.key == ProductSearchTypeKey && searchInput.isBlank() && currentValue.isBlank() -> {
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
                                onAction = onOpenType
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
    }
}

@Composable
private fun TagCommandCard(
    tagCount: Int,
    onAddTag: () -> Unit,
    onClearTags: () -> Unit
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
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Text(
                text = if (tagCount == 0) "Nenhuma tag adicionada" else "$tagCount tag(s) adicionada(s)",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextPrimary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                ActionButtonPrimary(
                    text = "Adicionar tag",
                    onClick = onAddTag,
                    modifier = Modifier.weight(1f)
                )
                ActionButtonOutline(
                    text = "Limpar tags",
                    onClick = onClearTags,
                    modifier = Modifier.weight(1f),
                    enabled = tagCount > 0
                )
            }
        }
    }
}

@Composable
private fun InlineNoticeCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.DangerSoft),
        border = BorderStroke(1.dp, AppColors.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md),
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.TextPrimary
        )
    }
}

@Composable
private fun SearchActionRow(
    currentType: String,
    currentValue: String,
    readingActive: Boolean,
    collectorLabel: String,
    collectorStatus: String,
    showPowerControl: Boolean,
    powerLevel: Int,
    onStartReading: () -> Unit,
    onStopReading: () -> Unit,
    onOpenType: () -> Unit,
    onClear: () -> Unit,
    onDecreasePower: () -> Unit,
    onIncreasePower: () -> Unit
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
                        label = "Iniciar",
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

            if (showPowerControl) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    Text(
                        text = "Potencia",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary
                    )
                    PowerStepButton(label = "-", onClick = onDecreasePower)
                    Text(
                        text = powerLevel.toString(),
                        modifier = Modifier
                            .border(1.dp, AppColors.Divider, AppShapes.input)
                            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.TextPrimary
                    )
                    PowerStepButton(label = "+", onClick = onIncreasePower)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                Text(
                    text = "$collectorLabel  |  $currentType",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )
                Text(
                    text = if (currentValue.isNotBlank()) currentValue else collectorStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TopBarBlue
                )
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
private fun PowerStepButton(
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = AppShapes.button,
        border = BorderStroke(1.dp, AppColors.Divider),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AppColors.FieldBackground,
            contentColor = AppColors.TextPrimary
        )
    ) {
        Text(text = label)
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
            Text(text = label, style = MaterialTheme.typography.bodySmall)
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
        ProductSearchTypeKey -> listOf(product.name, product.code, product.reducedCode, product.ean13)
        "reduced" -> listOf(product.reducedCode)
        "ean13" -> listOf(product.ean13)
        TagSearchTypeKey -> listOf(product.tagCode)
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
    return lowercase().replace(" ", "").replace(".", "").replace("-", "")
}

private fun String.normalizedTag(): String {
    return uppercase().replace(" ", "").replace("-", "").replace(".", "")
}

private fun rssiToPercent(rssi: Int?): Int {
    val value = rssi ?: return 0
    return if (value in 0..100) {
        value
    } else {
        (((value + 80f) / 45f) * 100f).toInt().coerceIn(0, 100)
    }
}

private fun proximityLabelFor(percent: Int): String {
    return when (percent.coerceIn(0, 100)) {
        in 0..33 -> "Longe"
        in 34..66 -> "Perto"
        else -> "Muito perto"
    }
}

private fun RfidTargetPreviewItem.toUiModel(): TagTargetItemUi {
    return TagTargetItemUi(
        epc = epc,
        proximityPercent = proximityPercent,
        proximityLabel = proximityLabel,
        matchedProductName = matchedProductName,
        lastSeenAtMillis = System.currentTimeMillis()
    )
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
private fun GlobalSearchScreenRfidPreview() {
    NexusRFIDTheme {
        GlobalSearchScreen(
            products = MockDataSource.products,
            searchSummary = MockDataSource.searchSummary,
            searchTargets = MockDataSource.searchTargets,
            searchTypes = MockDataSource.searchTypes,
            onMenuClick = {},
            initialSelectedTypeKey = TagSearchTypeKey,
            initialTagTargets = MockDataSource.rfidTagPreviewTargets
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
