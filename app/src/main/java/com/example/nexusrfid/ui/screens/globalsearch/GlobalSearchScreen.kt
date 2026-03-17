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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BluetoothConnected
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.data.mock.ProductListItem
import com.example.nexusrfid.data.mock.ProductTargetState
import com.example.nexusrfid.data.mock.RfidTargetPreviewItem
import com.example.nexusrfid.data.mock.SearchCounterSummary
import com.example.nexusrfid.data.mock.SearchTargetItem
import com.example.nexusrfid.data.mock.SearchTypeOption
import com.example.nexusrfid.rfid.CollectorModel
import com.example.nexusrfid.rfid.ReaderRecognitionState
import com.example.nexusrfid.rfid.RfidConnectionState
import com.example.nexusrfid.ui.app.NexusRfidAppState
import com.example.nexusrfid.ui.components.ActionButtonOutline
import com.example.nexusrfid.ui.components.ActionButtonPrimary
import com.example.nexusrfid.ui.components.AppDialog
import com.example.nexusrfid.ui.components.AppTopBar
import com.example.nexusrfid.ui.components.CounterBar
import com.example.nexusrfid.ui.components.EmptyStateBox
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
private const val TagExpectedLength = 20
private const val TagSuffixPadding = "0000"

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
    var showReadTagsDialog by remember { mutableStateOf(false) }
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
    val readTagRegistry = remember { mutableStateMapOf<String, ReadTagItemUi>() }

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
            products.firstOrNull { tagsMatch(it.tagCode, target.epc) }
        }
        .distinctBy { it.code }
        .filter { product ->
            selectedTarget.targetState == null || product.targetState == selectedTarget.targetState
        }

    val counterReadCount = if (isTagMode) readCount else searchSummary.readCount
    val counterFoundCount = if (isTagMode) foundCount else searchSummary.foundCount
    val soundEnabled = appState?.soundEnabled ?: true
    val readerReady = appState?.let { state ->
        state.connectionState == RfidConnectionState.Connected &&
            state.readerRecognitionState == ReaderRecognitionState.Recognized
    } ?: false

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
            readTagRegistry.clear()
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
            openDialogFor(option)
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
        readTagRegistry.clear()
        appState?.clearErrorMessage()
    }

    fun addTagTarget(rawValue: String) {
        val normalizedTag = rawValue.normalizedTag()
        when {
            !isValidTagLength(normalizedTag) -> dialogErrorMessage = "A tag precisa ter 20 caracteres."
            tagTargets.any { tagsMatch(it.epc, normalizedTag) } -> dialogErrorMessage = "Essa tag ja foi adicionada."
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
                    val updatedTargets = tagTargets.toMutableList()
                    val targetSignals = mutableMapOf<Int, Int>()
                    val targetProductNames = mutableMapOf<Int, String>()
                    batch.forEach { tagRead ->
                        val normalizedEpc = tagRead.epc.normalizedTag()
                        val canonicalEpc = canonicalTag(normalizedEpc)
                        val percent = rssiToPercent(tagRead.rssi)
                        val matchedProduct = products.firstOrNull { tagsMatch(it.tagCode, normalizedEpc) }
                        readTagRegistry[canonicalEpc] = ReadTagItemUi(
                            epc = canonicalEpc,
                            matchedProductName = matchedProduct?.name,
                            lastSeenAtMillis = now,
                            rssiPercent = percent
                        )
                        val matchedIndex = updatedTargets.indexOfFirst { tagsMatch(it.epc, normalizedEpc) }
                        if (matchedIndex >= 0) {
                            matchFoundInCycle = matchFoundInCycle || percent > 0
                            val current = targetSignals[matchedIndex]
                            targetSignals[matchedIndex] = if (current == null) {
                                percent
                            } else {
                                maxOf(current, percent)
                            }
                            if (!matchedProduct?.name.isNullOrBlank()) {
                                targetProductNames[matchedIndex] = matchedProduct.name
                            }
                        }
                    }
                    if (targetSignals.isNotEmpty()) {
                        targetSignals.forEach { (index, percent) ->
                            val existingTarget = updatedTargets[index]
                            updatedTargets[index] = existingTarget.copy(
                                proximityPercent = percent,
                                proximityLabel = proximityLabelFor(percent),
                                matchedProductName = targetProductNames[index] ?: existingTarget.matchedProductName,
                                lastSeenAtMillis = now
                            )
                        }
                    }
                    tagTargets = updatedTargets
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
                delay(120)
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

    val readTargets = readTagRegistry.values
        .sortedWith(compareByDescending<ReadTagItemUi> { it.lastSeenAtMillis }.thenBy { it.epc })

    GlobalSearchContent(
        products = products,
        searchTargets = searchTargets,
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
        onAddTag = { showTypeSheet = true },
        onClearTags = {
            stopRfidSearch(resetFeedback = true)
            tagTargets = emptyList()
        },
        onRemoveTag = { tagToRemove ->
            tagTargets = tagTargets.filterNot { it.epc == tagToRemove }
            refreshFoundCount()
        },
        soundEnabled = soundEnabled,
        readerReady = readerReady,
        onToggleSound = {
            appState?.let { state -> state.updateSoundEnabled(!state.soundEnabled) }
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
        collectorStatus = appState?.statusMessage.orEmpty(),
        connectedDeviceName = appState?.connectedDevice?.displayName,
        readingActive = rfidSearching,
        powerLevel = appState?.readerPower ?: 15,
        readCount = counterReadCount,
        foundCount = counterFoundCount,
        tagCount = tagTargets.size,
        searchInput = productInput,
        searchValue = searchValue,
        onSearchInputChange = { productInput = it },
        onTextSearch = { searchValue = productInput.trim() },
        filteredProducts = filteredProducts,
        matchedProducts = matchedProducts,
        tagTargets = tagTargets,
        errorMessage = appState?.errorMessage ?: dialogErrorMessage,
        onReadCountClick = if (isTagMode && readTargets.isNotEmpty()) {
            { showReadTagsDialog = true }
        } else {
            null
        },
        modifier = modifier
    )

    if (showTypeSheet) {
        SearchTypeSheet(
            options = searchTypes,
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
                TagSearchTypeKey -> isValidTagLength(dialogValue.normalizedTag())
                else -> dialogValue.isNotBlank()
            }
        )
    }

    if (showReadTagsDialog) {
        ReadTagsDialog(
            items = readTargets,
            onDismiss = { showReadTagsDialog = false }
        )
    }
}

@Composable
private fun SearchOverviewCard(
    currentType: String,
    isTagMode: Boolean,
    collectorLabel: String,
    collectorStatus: String,
    connectedDeviceName: String?,
    readingActive: Boolean,
    powerLevel: Int,
    searchValue: String,
    tagCount: Int,
    onStartSearch: () -> Unit,
    onStopSearch: () -> Unit,
    onOpenType: () -> Unit,
    onClear: () -> Unit,
    onDecreasePower: () -> Unit,
    onIncreasePower: () -> Unit
) {
    val headline = if (isTagMode) "Busca RFID operacional" else "Consulta de produtos"
    val statusLine = when {
        isTagMode && readingActive -> "Localizacao em andamento"
        isTagMode && tagCount > 0 -> "$tagCount tag(s) pronta(s) para iniciar"
        isTagMode -> "Adicione as tags alvo"
        searchValue.isNotBlank() -> "Ultima busca: $searchValue"
        else -> "Escolha o tipo e preencha a consulta"
    }

    val actionTiles = if (isTagMode) {
        listOf(
            ActionTileSpec("Iniciar", Icons.Outlined.PlayArrow, onStartSearch, active = readingActive),
            ActionTileSpec("Parar", Icons.Outlined.Stop, onStopSearch),
            ActionTileSpec("Tipo", Icons.Outlined.Tune, onOpenType),
            ActionTileSpec("Limpar", Icons.Outlined.DeleteSweep, onClear)
        )
    } else {
        listOf(
            ActionTileSpec("Buscar", Icons.Outlined.Search, onStartSearch),
            ActionTileSpec("Tipo", Icons.Outlined.Tune, onOpenType),
            ActionTileSpec("Limpar", Icons.Outlined.DeleteSweep, onClear)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
        border = BorderStroke(1.dp, AppColors.AccentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                Text(
                    text = "BUSCA",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                    color = AppColors.TextSecondary
                )
                Text(
                    text = headline,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.TextPrimary
                )
                Text(
                    text = statusLine,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextSecondary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                OperationChip(text = currentType)
                if (collectorLabel.isNotBlank()) {
                    OperationChip(text = collectorLabel)
                }
                if (connectedDeviceName != null) {
                    OperationChip(text = connectedDeviceName, accent = true)
                } else if (isTagMode) {
                    OperationChip(text = "Sem coletor")
                }
                if (isTagMode) {
                    OperationChip(text = "$tagCount tag(s)")
                }
            }

            Text(
                text = if (collectorStatus.isNotBlank()) collectorStatus else "Fluxo pronto para busca imediata.",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary
            )

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val spacing = AppSpacing.sm
                val itemWidth = (maxWidth - (spacing * (actionTiles.size - 1))) / actionTiles.size

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    actionTiles.forEach { tile ->
                        ActionTile(
                            label = tile.label,
                            icon = tile.icon,
                            active = tile.active,
                            modifier = Modifier.width(itemWidth),
                            onClick = tile.onClick
                        )
                    }
                }
            }

            if (isTagMode) {
                PowerControlStrip(
                    powerLevel = powerLevel,
                    onDecreasePower = onDecreasePower,
                    onIncreasePower = onIncreasePower
                )
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
        colors = CardDefaults.cardColors(containerColor = AppColors.AccentSurface),
        border = BorderStroke(1.dp, AppColors.AccentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                    Text(
                        text = "TAGS",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                        color = AppColors.TextSecondary
                    )
                    Text(
                        text = if (tagCount == 0) "Monte a lista de busca" else "$tagCount tag(s) adicionada(s)",
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.TextPrimary
                    )
                }
                Text(
                    text = "EPC 20",
                    style = MaterialTheme.typography.labelLarge,
                    color = AppColors.TopBarBlue
                )
            }

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
                    text = "Limpar lista",
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.BluetoothConnected,
                contentDescription = null,
                tint = AppColors.TextPrimary
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextPrimary
            )
        }
    }
}

@Composable
private fun ActionTile(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(74.dp),
        shape = AppShapes.button,
        border = BorderStroke(
            width = 1.dp,
            color = if (active) AppColors.PrimaryActionBlue else AppColors.Divider
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (active) AppColors.AccentSurface else AppColors.FieldBackground,
            contentColor = if (active) AppColors.TopBarBlue else AppColors.TextPrimary
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 10.sp,
                    letterSpacing = 0.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
        }
    }
}

@Composable
private fun PowerControlStrip(
    powerLevel: Int,
    onDecreasePower: () -> Unit,
    onIncreasePower: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.FieldBackground, AppShapes.card)
            .border(1.dp, AppColors.Divider, AppShapes.card)
            .padding(AppSpacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
            Text(
                text = "POTENCIA",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                color = AppColors.TextSecondary
            )
            Text(
                text = "Ajuste rapido do leitor",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PowerStepButton(label = "-", onClick = onDecreasePower)
            Text(
                text = powerLevel.toString(),
                modifier = Modifier
                    .border(1.dp, AppColors.Divider, AppShapes.input)
                    .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
                style = MaterialTheme.typography.titleSmall.copy(fontFamily = FontFamily.Monospace),
                color = AppColors.TextPrimary
            )
            PowerStepButton(label = "+", onClick = onIncreasePower, filled = true)
        }
    }
}

@Composable
private fun PowerStepButton(
    label: String,
    onClick: () -> Unit,
    filled: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        shape = AppShapes.button,
        border = BorderStroke(1.dp, AppColors.Divider),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (filled) AppColors.PrimaryActionBlue else AppColors.CardSurface,
            contentColor = if (filled) AppColors.TopBarOnBlue else AppColors.TextPrimary
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            ),
            color = if (filled) AppColors.TopBarOnBlue else AppColors.TextPrimary
        )
    }
}

@Composable
private fun OperationChip(
    text: String,
    accent: Boolean = false
) {
    Box(
        modifier = Modifier
            .background(
                if (accent) AppColors.BrandSignalBlue.copy(alpha = 0.14f) else AppColors.CardSurfaceHighlight,
                AppShapes.button
            )
            .border(
                0.5.dp,
                if (accent) AppColors.BrandSignalBlue else AppColors.Divider,
                AppShapes.button
            )
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
            color = if (accent) AppColors.BrandSignalBlue else AppColors.TopBarOnBlue
        )
    }
}

private data class ActionTileSpec(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val active: Boolean = false
)

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
        border = BorderStroke(1.dp, AppColors.AccentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                Text(
                    text = "TARGETS",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                    color = AppColors.TextSecondary
                )
                Text(
                    text = "Filtrar a exibicao dos resultados",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )
            }

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
            color = if (selected) AppColors.PrimaryActionBlue else AppColors.Divider
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) AppColors.BrandSignalBlue.copy(alpha = 0.14f) else AppColors.CardSurfaceHighlight,
            contentColor = if (selected) AppColors.BrandSignalBlue else AppColors.TextSecondary
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp)
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(AppColors.CardSurface, AppShapes.button)
                    .border(1.dp, if (selected) AppColors.BrandSignalBlue else AppColors.Divider, AppShapes.button),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) AppColors.BrandSignalBlue else AppColors.TextSecondary
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
        border = BorderStroke(1.dp, AppColors.AccentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
            ) {
                Text(
                    text = "RESULTADOS",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                    color = AppColors.TextSecondary
                )
                Text(
                    text = "${products.size} item(ns) em $selectedTarget",
                    style = MaterialTheme.typography.titleSmall,
                    color = AppColors.TextPrimary
                )
            }

            products.forEachIndexed { index, product ->
                SimpleListRow(
                    title = product.name,
                    subtitle = "Cod. ${product.code}  |  ${targetStateLabel(product.targetState)}  |  ${product.tagCode.takeLast(8)}",
                    showDivider = index < products.lastIndex
                )
            }
        }
    }
}

@Composable
private fun LegacyActionRow(
    readingActive: Boolean,
    soundEnabled: Boolean,
    readerEnabled: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onPowerClick: () -> Unit,
    onSoundClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LegacyActionButton(
            label = "Iniciar",
            icon = Icons.Outlined.PlayArrow,
            onClick = onStart,
            active = readingActive,
            enabled = readerEnabled,
            modifier = Modifier.weight(1f)
        )
        LegacyActionButton(
            label = "Parar",
            icon = Icons.Outlined.Stop,
            onClick = onStop,
            enabled = readerEnabled,
            modifier = Modifier.weight(1f)
        )
        LegacyActionButton(
            label = "Potencia",
            icon = Icons.Outlined.Wifi,
            onClick = onPowerClick,
            active = readerEnabled,
            enabled = readerEnabled,
            modifier = Modifier.weight(1f)
        )
        LegacyActionButton(
            label = "Som",
            icon = if (soundEnabled) Icons.Outlined.VolumeUp else Icons.Outlined.VolumeOff,
            onClick = onSoundClick,
            active = soundEnabled,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LegacyActionButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    active: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val disabledColor = AppColors.TextSecondary.copy(alpha = 0.4f)
    val tint = when {
        !enabled -> disabledColor
        active -> AppColors.BrandSignalBlue
        else -> AppColors.TextSecondary
    }
    val textColor = if (enabled) AppColors.TextSecondary else disabledColor
    Column(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = AppSpacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Composable
private fun LegacyTargetsCard(
    readCount: Int,
    foundCount: Int,
    tagCount: Int,
    onAddTarget: () -> Unit,
    onClearTargets: () -> Unit,
    onReadCountClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val message = if (tagCount == 0) {
        "Nenhum target para buscar ..."
    } else {
        "$tagCount target(s) adicionados"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
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
            CounterBar(
                readCount = readCount,
                foundCount = foundCount,
                onReadClick = onReadCountClick
            )
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                LegacyTargetButton(
                    text = "ADICIONAR TARGETS",
                    onClick = onAddTarget,
                    modifier = Modifier.weight(1f),
                    borderColor = AppColors.PositiveGreen.copy(alpha = 0.6f),
                    contentColor = AppColors.PositiveGreen
                )
                LegacyTargetButton(
                    text = "REMOVER TARGETS",
                    onClick = onClearTargets,
                    modifier = Modifier.weight(1f),
                    borderColor = AppColors.DangerRed.copy(alpha = 0.6f),
                    contentColor = AppColors.DangerRed
                )
            }
        }
    }
}

@Composable
private fun LegacyTargetButton(
    text: String,
    onClick: () -> Unit,
    borderColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = AppShapes.button,
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AppColors.CardSurface,
            contentColor = contentColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor
        )
    }
}

@Composable
private fun ReadTagsDialog(
    items: List<ReadTagItemUi>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = AppShapes.modal,
            colors = CardDefaults.cardColors(containerColor = AppColors.DarkModal),
            border = BorderStroke(1.dp, AppColors.BrandSignalBlue.copy(alpha = 0.44f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                        Text(
                            text = "PECAS LIDAS",
                            style = MaterialTheme.typography.labelLarge,
                            color = AppColors.BrandSignalBlue
                        )
                        Text(
                            text = "${items.size} item(s) lido(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Fechar",
                            tint = AppColors.TextSecondary
                        )
                    }
                }

                if (items.isEmpty()) {
                    Text(
                        text = "Nenhuma tag lida ainda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                    ) {
                        items(items, key = { it.epc }) { item ->
                            ReadTagRow(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReadTagRow(
    item: ReadTagItemUi
) {
    val title = item.matchedProductName ?: "Tag ${item.epc.takeLast(8)}"
    val status = if (item.rssiPercent > 0) proximityLabelFor(item.rssiPercent) else "Lida"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, AppColors.Divider, AppShapes.card)
            .background(AppColors.CardSurfaceHighlight, AppShapes.card)
            .padding(AppSpacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TopBarOnBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.epc,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                color = AppColors.TextSecondary
            )
        }
        Text(
            text = status,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
            color = AppColors.BrandSignalBlue
        )
    }
}

@Composable
private fun PowerDialog(
    powerLevel: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = AppShapes.modal,
            colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
            border = BorderStroke(1.dp, AppColors.Divider),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                Text(
                    text = "Potencia do leitor",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PowerStepButton(label = "-", onClick = onDecrease)
                    Text(
                        text = powerLevel.toString(),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, AppColors.Divider, AppShapes.input)
                            .padding(vertical = AppSpacing.sm),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = AppColors.TextPrimary
                    )
                    PowerStepButton(label = "+", onClick = onIncrease, filled = true)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) {
                        Text(
                            text = "Fechar",
                            color = AppColors.TextSecondary
                        )
                    }
                }
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

private data class ReadTagItemUi(
    val epc: String,
    val matchedProductName: String? = null,
    val lastSeenAtMillis: Long,
    val rssiPercent: Int
)

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

private fun isValidTagLength(value: String): Boolean {
    return value.length == TagExpectedLength
}

private fun canonicalTag(value: String): String {
    val normalized = value.normalizedTag()
    return if (normalized.length == TagExpectedLength + TagSuffixPadding.length &&
        normalized.endsWith(TagSuffixPadding)
    ) {
        normalized.dropLast(TagSuffixPadding.length)
    } else {
        normalized
    }
}

private fun tagVariants(value: String): Set<String> {
    val normalized = value.normalizedTag()
    if (normalized.isBlank()) return emptySet()
    val variants = mutableSetOf(normalized)
    if (normalized.length == TagExpectedLength) {
        variants.add(normalized.padStart(TagExpectedLength + TagSuffixPadding.length, '0'))
        variants.add(normalized + TagSuffixPadding)
    } else if (normalized.length == TagExpectedLength + TagSuffixPadding.length) {
        val trimmedStart = normalized.trimStart('0')
        if (trimmedStart.length == TagExpectedLength) {
            variants.add(trimmedStart)
        }
        if (normalized.endsWith(TagSuffixPadding)) {
            val trimmedEnd = normalized.dropLast(TagSuffixPadding.length)
            if (trimmedEnd.length == TagExpectedLength) {
                variants.add(trimmedEnd)
            }
        }
    }
    return variants
}

private fun tagsMatch(left: String, right: String): Boolean {
    if (left.isBlank() || right.isBlank()) return false
    val leftVariants = tagVariants(left)
    val rightVariants = tagVariants(right)
    return leftVariants.any { it in rightVariants }
}

private fun rssiToPercent(rssi: Int?): Int {
    val value = rssi ?: return 0
    return when {
        value in 0..100 -> value
        value < 0 -> {
            val clamped = value.coerceIn(-120, -30)
            (((clamped + 120f) / 90f) * 100f).toInt().coerceIn(0, 100)
        }
        else -> 100
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

@Composable
private fun GlobalSearchContent(
    products: List<ProductListItem>,
    searchTargets: List<SearchTargetItem>,
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
    soundEnabled: Boolean,
    readerReady: Boolean,
    onToggleSound: () -> Unit,
    onDecreasePower: () -> Unit,
    onIncreasePower: () -> Unit,
    collectorLabel: String,
    collectorStatus: String,
    connectedDeviceName: String?,
    readingActive: Boolean,
    powerLevel: Int,
    readCount: Int,
    foundCount: Int,
    tagCount: Int,
    searchInput: String,
    searchValue: String,
    onSearchInputChange: (String) -> Unit,
    onTextSearch: () -> Unit,
    filteredProducts: List<ProductListItem>,
    matchedProducts: List<ProductListItem>,
    tagTargets: List<TagTargetItemUi>,
    errorMessage: String?,
    onReadCountClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val baseBackground = AppColors.ScreenBackground
    var showPowerDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseBackground)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = baseBackground,
            topBar = {
                AppTopBar(
                    title = "Busca Global",
                    eyebrow = null,
                    onNavigationClick = onMenuClick,
                    navigationIconBackground = false,
                    showDivider = false
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(baseBackground)
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                if (isTagMode) {
                    item {
                        LegacyActionRow(
                            readingActive = readingActive,
                            soundEnabled = soundEnabled,
                            readerEnabled = readerReady,
                            onStart = onStartSearch,
                            onStop = onStopSearch,
                            onPowerClick = {
                                if (readerReady) {
                                    showPowerDialog = true
                                }
                            },
                            onSoundClick = onToggleSound
                        )
                    }

                    item {
                        LegacyTargetsCard(
                            readCount = readCount,
                            foundCount = foundCount,
                            tagCount = tagCount,
                            onAddTarget = onAddTag,
                            onClearTargets = onClearTags,
                            onReadCountClick = onReadCountClick
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

                    if (!errorMessage.isNullOrBlank()) {
                        item {
                            InlineNoticeCard(message = errorMessage)
                        }
                    }

                    if (matchedProducts.isNotEmpty()) {
                        item {
                            SearchResultsCard(
                                products = matchedProducts,
                                selectedTarget = selectedTarget.label
                            )
                        }
                    }
                } else {
                    item {
                        SearchOverviewCard(
                            currentType = selectedType.label,
                            isTagMode = false,
                            collectorLabel = collectorLabel,
                            collectorStatus = collectorStatus,
                            connectedDeviceName = connectedDeviceName,
                            readingActive = readingActive,
                            powerLevel = powerLevel,
                            searchValue = searchValue,
                            tagCount = tagCount,
                            onStartSearch = onStartSearch,
                            onStopSearch = onStopSearch,
                            onOpenType = onOpenType,
                            onClear = onClear,
                            onDecreasePower = onDecreasePower,
                            onIncreasePower = onIncreasePower
                        )
                    }

                    item {
                        SearchHeader(
                            value = searchInput,
                            onValueChange = onSearchInputChange,
                            onSearchClick = onTextSearch,
                            sectionTitle = "Consulta",
                            placeholder = "Nome, codigo, reduzido ou EAN"
                        )
                    }

                    item {
                        CounterBar(
                            readCount = readCount,
                            foundCount = foundCount,
                            onReadClick = onReadCountClick
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

                    if (!errorMessage.isNullOrBlank()) {
                        item {
                            InlineNoticeCard(message = errorMessage)
                        }
                    }

                    item {
                        when {
                            selectedType.requiresManualEntry && searchValue.isBlank() -> {
                                EmptyStateBox(
                                    title = "Informe o valor da consulta",
                                    supportingText = "Use ${selectedType.label} para localizar o item.",
                                    actionLabel = "Escolher ${selectedType.label}",
                                    onAction = onOpenType
                                )
                            }

                            selectedType.key == ProductSearchTypeKey && searchInput.isBlank() && searchValue.isBlank() -> {
                                EmptyStateBox(
                                    title = "Digite o produto",
                                    supportingText = "Informe nome ou codigo e use Buscar."
                                )
                            }

                            filteredProducts.isEmpty() -> {
                                EmptyStateBox(
                                    title = "Nenhum produto localizado",
                                    supportingText = "Tente outro valor ou mude o tipo de busca.",
                                    actionLabel = "Trocar tipo",
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

    if (showPowerDialog) {
        PowerDialog(
            powerLevel = powerLevel,
            onDecrease = onDecreasePower,
            onIncrease = onIncreasePower,
            onDismiss = { showPowerDialog = false }
        )
    }
}
