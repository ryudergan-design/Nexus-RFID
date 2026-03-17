package com.example.nexusrfid.ui.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BluetoothConnected
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.rfid.CollectorModel
import com.example.nexusrfid.rfid.ReaderRecognitionFeedback
import com.example.nexusrfid.rfid.RfidConnectionState
import com.example.nexusrfid.rfid.RfidDevice
import com.example.nexusrfid.rfid.RfidPermissionGateway
import com.example.nexusrfid.ui.app.NexusRfidAppState
import com.example.nexusrfid.ui.components.ActionButtonOutline
import com.example.nexusrfid.ui.components.ActionButtonPrimary
import com.example.nexusrfid.ui.components.AppTopBar
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

@Composable
fun SettingsScreen(
    appState: NexusRfidAppState,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (RfidPermissionGateway.isBluetoothEnabled(context)) {
            appState.startR6Discovery()
        } else {
            appState.reportError("Ative o Bluetooth para listar leitores pareados.")
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.all { it }) {
            if (RfidPermissionGateway.isBluetoothEnabled(context)) {
                appState.startR6Discovery()
            } else {
                enableBluetoothLauncher.launch(RfidPermissionGateway.bluetoothEnableIntent())
            }
        } else {
            appState.reportError("Permita Bluetooth para listar leitores pareados.")
        }
    }

    fun startDeviceDiscovery() {
        appState.clearErrorMessage()
        when (appState.selectedCollectorModel) {
            CollectorModel.R6 -> {
                val missing = RfidPermissionGateway.missingPermissions(context)
                when {
                    missing.isNotEmpty() -> permissionLauncher.launch(missing)
                    !RfidPermissionGateway.isBluetoothEnabled(context) -> {
                        enableBluetoothLauncher.launch(RfidPermissionGateway.bluetoothEnableIntent())
                    }
                    else -> appState.startR6Discovery()
                }
            }
            CollectorModel.MC339U -> appState.startR6Discovery()
            CollectorModel.C72 -> Unit
        }
    }

    SettingsScreenPreviewContent(
        onMenuClick = onMenuClick,
        selectedCollectorModel = appState.selectedCollectorModel,
        connectionState = appState.connectionState,
        statusMessage = appState.statusMessage,
        connectedDevice = appState.connectedDevice,
        soundEnabled = appState.soundEnabled,
        isSearchingDevices = appState.isDeviceScanRunning,
        availableDevices = appState.availableDevices,
        recognitionFeedback = appState.recognitionFeedback,
        errorMessage = appState.errorMessage,
        onCollectorModelSelected = appState::selectCollectorModel,
        onSoundChange = appState::updateSoundEnabled,
        onStartDeviceDiscovery = ::startDeviceDiscovery,
        onStopDeviceScan = appState::stopDeviceScan,
        onConnectDevice = appState::connectToR6,
        onConnectInternalCollector = appState::connectInternalCollector,
        onDisconnect = appState::disconnectReader,
        onDismissRecognition = appState::clearRecognitionFeedback,
        onDismissError = appState::clearErrorMessage,
        modifier = modifier
    )
}

@Composable
fun SettingsScreenPreviewContent(
    onMenuClick: () -> Unit,
    selectedCollectorModel: CollectorModel,
    connectionState: RfidConnectionState,
    statusMessage: String,
    connectedDevice: RfidDevice?,
    soundEnabled: Boolean,
    isSearchingDevices: Boolean,
    availableDevices: List<RfidDevice>,
    recognitionFeedback: ReaderRecognitionFeedback?,
    errorMessage: String?,
    onCollectorModelSelected: (CollectorModel) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onStartDeviceDiscovery: () -> Unit,
    onStopDeviceScan: () -> Unit,
    onConnectDevice: (RfidDevice) -> Unit,
    onConnectInternalCollector: () -> Unit,
    onDisconnect: () -> Unit,
    onDismissRecognition: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnecting = connectionState == RfidConnectionState.Connecting
    val isConnected = connectionState == RfidConnectionState.Connected
    var showCollectorPicker by remember { mutableStateOf(false) }
    var showDevicePicker by remember { mutableStateOf(false) }
    val deviceAvailabilityMessage = when {
        isSearchingDevices -> "Buscando leitores disponiveis..."
        availableDevices.isEmpty() -> {
            if (selectedCollectorModel == CollectorModel.R6) {
                "Nenhum leitor pareado encontrado."
            } else {
                "Nenhum leitor disponivel encontrado."
            }
        }
        availableDevices.size == 1 -> {
            if (selectedCollectorModel == CollectorModel.R6) {
                "1 leitor pareado pronto para conectar."
            } else {
                "1 leitor disponivel encontrado."
            }
        }
        else -> {
            if (selectedCollectorModel == CollectorModel.R6) {
                "${availableDevices.size} leitores pareados prontos para conectar."
            } else {
                "${availableDevices.size} leitores disponiveis encontrados."
            }
        }
    }

    fun openDevicePicker() {
        onStartDeviceDiscovery()
        showDevicePicker = true
    }

    fun closeDevicePicker() {
        showDevicePicker = false
        onStopDeviceScan()
    }

    fun openCollectorPicker() {
        showCollectorPicker = true
    }

    fun closeCollectorPicker() {
        showCollectorPicker = false
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.ScreenBackground,
        topBar = {
            AppTopBar(
                title = "Configuracoes",
                eyebrow = "Nexus RFID",
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
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            item {
                SettingsHeroCard(
                    selectedCollectorModel = selectedCollectorModel,
                    connectionState = connectionState,
                    statusMessage = statusMessage,
                    connectedDevice = connectedDevice
                )
            }

            item {
                SettingsSectionCard(
                    eyebrow = "COLETOR",
                    title = "Modelo RFID"
                ) {
                    Text(
                        text = "Toque na barra abaixo para selecionar o modelo do coletor.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary
                    )
                    CollectorPickerField(
                        selectedModel = selectedCollectorModel,
                        onClick = ::openCollectorPicker
                    )
                }
            }

            item {
                SettingsSectionCard(
                    eyebrow = "ALERTA",
                    title = "Som de proximidade"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (soundEnabled) {
                                        Icons.Outlined.NotificationsActive
                                    } else {
                                        Icons.Outlined.NotificationsOff
                                    },
                                    contentDescription = null,
                                    tint = AppColors.TopBarBlue
                                )
                                Text(
                                    text = if (soundEnabled) "Alerta ativo" else "Alerta silencioso",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = AppColors.TextPrimary
                                )
                            }
                            Text(
                                text = "Desligue o som quando a busca precisar ser discreta.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TextSecondary
                            )
                        }

                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = onSoundChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AppColors.TopBarOnBlue,
                                checkedTrackColor = AppColors.PrimaryActionBlue,
                                uncheckedThumbColor = AppColors.CardSurface,
                                uncheckedTrackColor = AppColors.MutedSurface
                            )
                        )
                    }
                }
            }

            if (selectedCollectorModel != CollectorModel.C72) {
                item {
                    SettingsSectionCard(
                        eyebrow = if (selectedCollectorModel == CollectorModel.R6) "BLUETOOTH" else "ZEBRA",
                        title = "Leitor ativo"
                    ) {
                        Text(
                            text = if (selectedCollectorModel == CollectorModel.R6) {
                                "Toque na caixa abaixo para listar leitores Bluetooth pareados."
                            } else {
                                "Toque na caixa abaixo para inicializar o servico Zebra."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )

                        DevicePickerField(
                            value = connectedDevice?.displayName,
                            placeholder = if (isSearchingDevices) {
                                if (selectedCollectorModel == CollectorModel.R6) "Listando leitores pareados..." else "Iniciando servico..."
                            } else {
                                if (selectedCollectorModel == CollectorModel.R6) "Listar leitores pareados" else "Abrir servico Zebra"
                            },
                            onClick = ::openDevicePicker
                        )

                        Text(
                            text = deviceAvailabilityMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )

                        if (connectionState == RfidConnectionState.Connected) {
                            ActionButtonOutline(
                                text = "Desconectar",
                                onClick = onDisconnect,
                                borderColor = AppColors.Divider,
                                containerColor = AppColors.CardSurfaceHighlight,
                                contentColor = AppColors.TopBarOnBlue
                            )
                        }
                    }
                }

            } else {
                item {
                    SettingsSectionCard(
                        eyebrow = "C72",
                        title = "Leitor interno"
                    ) {
                        Text(
                            text = "Conecte o leitor interno para validar a comunicacao com o coletor.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )
                        ActionButtonPrimary(
                            text = when {
                                isConnected -> "C72 conectado"
                                isConnecting -> "Conectando..."
                                else -> "Conectar C72"
                            },
                            onClick = onConnectInternalCollector,
                            enabled = !isConnected && !isConnecting,
                            modifier = Modifier.height(56.dp),
                            textStyle = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                        )
                        if (isConnected) {
                            ActionButtonOutline(
                                text = "Desconectar",
                                onClick = onDisconnect,
                                borderColor = AppColors.Divider,
                                containerColor = AppColors.CardSurfaceHighlight,
                                contentColor = AppColors.TopBarOnBlue
                            )
                        }
                    }
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                item {
                    ErrorCard(
                        message = errorMessage,
                        onDismissError = onDismissError
                    )
                }
            }
        }
    }

    if (showDevicePicker) {
        DevicePickerDialog(
            devices = availableDevices,
            isSearching = isSearchingDevices,
            selectedDevice = connectedDevice,
            onSelect = { device ->
                onConnectDevice(device)
                closeDevicePicker()
            },
            onDismiss = ::closeDevicePicker
        )
    }

    if (showCollectorPicker) {
        CollectorPickerDialog(
            selectedModel = selectedCollectorModel,
            onSelect = { model ->
                onCollectorModelSelected(model)
                closeCollectorPicker()
            },
            onDismiss = ::closeCollectorPicker
        )
    }

    recognitionFeedback?.let { feedback ->
        ReaderRecognitionDialog(
            recognized = feedback.recognized,
            onDismiss = onDismissRecognition
        )
    }
}

@Composable
private fun SettingsHeroCard(
    selectedCollectorModel: CollectorModel,
    connectionState: RfidConnectionState,
    statusMessage: String,
    connectedDevice: RfidDevice?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.TopBarBlue),
        border = BorderStroke(1.dp, AppColors.TopBarOnBlue.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                    Text(
                        text = "OPERACAO DO COLETOR",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                        color = AppColors.BrandSignalBlue
                    )
                    Text(
                        text = when (selectedCollectorModel) {
                            CollectorModel.R6 -> "Conexao pronta para o R6"
                            CollectorModel.C72 -> "C72 selecionado"
                            CollectorModel.MC339U -> "MC339U selecionado"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppColors.TopBarOnBlue
                    )
                }

                Box(
                    modifier = Modifier
                        .background(AppColors.TopBarOnBlue.copy(alpha = 0.08f), CircleShape)
                        .padding(AppSpacing.sm),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (selectedCollectorModel) {
                            CollectorModel.R6 -> Icons.Outlined.BluetoothConnected
                            CollectorModel.C72 -> Icons.Outlined.Memory
                            CollectorModel.MC339U -> Icons.Outlined.Devices
                        },
                        contentDescription = null,
                        tint = AppColors.TopBarOnBlue
                    )
                }
            }

            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TopBarOnBlue.copy(alpha = 0.84f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                StatusPill(
                    text = selectedCollectorModel.label,
                    background = AppColors.TopBarOnBlue.copy(alpha = 0.10f),
                    contentColor = AppColors.TopBarOnBlue
                )
                StatusPill(
                    text = when (connectionState) {
                        RfidConnectionState.Connected -> "Conectado"
                        RfidConnectionState.Connecting -> "Conectando"
                        else -> "Pronto"
                    },
                    background = AppColors.TopBarOnBlue.copy(alpha = 0.10f),
                    contentColor = AppColors.TopBarOnBlue
                )
                if (connectedDevice != null) {
                    StatusPill(
                        text = connectedDevice.displayName,
                        background = AppColors.TopBarOnBlue.copy(alpha = 0.10f),
                        contentColor = AppColors.TopBarOnBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    eyebrow: String,
    title: String,
    content: @Composable () -> Unit
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
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                Text(
                    text = eyebrow,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                    color = AppColors.TextSecondary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.TextPrimary
                )
            }
            content()
        }
    }
}

@Composable
private fun DevicePickerField(
    value: String?,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayText = value?.takeIf { it.isNotBlank() } ?: placeholder
    val textColor = if (value.isNullOrBlank()) AppColors.TextSecondary else AppColors.TextPrimary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, AppColors.Divider, AppShapes.input)
            .background(AppColors.FieldBackground, AppShapes.input)
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.BluetoothConnected,
            contentDescription = null,
            tint = AppColors.TopBarBlue
        )
        Text(
            text = displayText,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = AppColors.TextSecondary
        )
    }
}

@Composable
private fun CollectorPickerField(
    selectedModel: CollectorModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, AppColors.Divider, AppShapes.input)
            .background(AppColors.FieldBackground, AppShapes.input)
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = collectorIcon(selectedModel),
            contentDescription = null,
            tint = AppColors.TopBarBlue
        )
        Text(
            text = collectorLabel(selectedModel),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.TextPrimary
        )
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = AppColors.TextSecondary
        )
    }
}

@Composable
private fun DevicePickerDialog(
    devices: List<RfidDevice>,
    isSearching: Boolean,
    selectedDevice: RfidDevice?,
    onSelect: (RfidDevice) -> Unit,
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
                Text(
                    text = "LEITORES DISPONIVEIS",
                    style = MaterialTheme.typography.labelLarge,
                    color = AppColors.BrandSignalBlue
                )
                Text(
                    text = if (isSearching) {
                        "Buscando leitores disponiveis..."
                    } else {
                        "Selecione um leitor disponivel para conexao."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )

                if (devices.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppSpacing.xl),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSearching) {
                                "Sincronizando leitores..."
                            } else {
                                "Nenhum leitor disponivel."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                    ) {
                        items(devices, key = { it.address }) { device ->
                            DeviceRow(
                                device = device,
                                selected = selectedDevice?.address == device.address,
                                onSelect = { onSelect(device) }
                            )
                        }
                    }
                }

                ActionButtonOutline(
                    text = "FECHAR",
                    onClick = onDismiss,
                    borderColor = AppColors.Divider,
                    containerColor = AppColors.CardSurfaceHighlight,
                    contentColor = AppColors.TopBarOnBlue
                )
            }
        }
    }
}

@Composable
private fun CollectorPickerDialog(
    selectedModel: CollectorModel,
    onSelect: (CollectorModel) -> Unit,
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
                Text(
                    text = "COLETORES DISPONIVEIS",
                    style = MaterialTheme.typography.labelLarge,
                    color = AppColors.BrandSignalBlue
                )
                Text(
                    text = "Escolha o modelo do coletor para configurar a conexao.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    CollectorRow(
                        model = CollectorModel.C72,
                        selected = selectedModel == CollectorModel.C72,
                        onSelect = { onSelect(CollectorModel.C72) }
                    )
                    CollectorRow(
                        model = CollectorModel.R6,
                        selected = selectedModel == CollectorModel.R6,
                        onSelect = { onSelect(CollectorModel.R6) }
                    )
                    CollectorRow(
                        model = CollectorModel.MC339U,
                        selected = selectedModel == CollectorModel.MC339U,
                        onSelect = { onSelect(CollectorModel.MC339U) }
                    )
                }

                ActionButtonOutline(
                    text = "FECHAR",
                    onClick = onDismiss,
                    borderColor = AppColors.Divider,
                    containerColor = AppColors.CardSurfaceHighlight,
                    contentColor = AppColors.TopBarOnBlue
                )
            }
        }
    }
}

@Composable
private fun DeviceRow(
    device: RfidDevice,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (selected) AppColors.BrandSignalBlue else AppColors.Divider
    val backgroundColor = if (selected) AppColors.BrandSignalBlue.copy(alpha = 0.12f) else AppColors.CardSurfaceHighlight

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, borderColor, AppShapes.card)
            .background(backgroundColor, AppShapes.card)
            .clickable(enabled = !selected, onClick = onSelect)
            .padding(AppSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(AppColors.CardSurface, CircleShape)
                .border(0.5.dp, AppColors.Divider, CircleShape)
                .padding(AppSpacing.sm),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Devices,
                contentDescription = null,
                tint = if (selected) AppColors.BrandSignalBlue else AppColors.TextSecondary
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Text(
                text = device.displayName.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TopBarOnBlue
            )
            Text(
                text = device.address,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                color = AppColors.TextSecondary
            )
        }

        if (selected) {
            StatusPill(
                text = "CONECTADO",
                background = AppColors.PositiveGreen.copy(alpha = 0.16f),
                contentColor = AppColors.PositiveGreen
            )
        } else {
            Text(
                text = "CONECTAR",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                color = AppColors.BrandSignalBlue
            )
        }
    }
}

@Composable
private fun CollectorRow(
    model: CollectorModel,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (selected) AppColors.BrandSignalBlue else AppColors.Divider
    val backgroundColor = if (selected) AppColors.BrandSignalBlue.copy(alpha = 0.12f) else AppColors.CardSurfaceHighlight

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, borderColor, AppShapes.card)
            .background(backgroundColor, AppShapes.card)
            .clickable(enabled = !selected, onClick = onSelect)
            .padding(AppSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(AppColors.CardSurface, CircleShape)
                .border(0.5.dp, AppColors.Divider, CircleShape)
                .padding(AppSpacing.sm),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = collectorIcon(model),
                contentDescription = null,
                tint = if (selected) AppColors.BrandSignalBlue else AppColors.TextSecondary
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Text(
                text = collectorLabel(model),
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TopBarOnBlue
            )
            Text(
                text = collectorSubtitle(model),
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary
            )
        }

        if (selected) {
            StatusPill(
                text = "SELECIONADO",
                background = AppColors.PositiveGreen.copy(alpha = 0.16f),
                contentColor = AppColors.PositiveGreen
            )
        } else {
            Text(
                text = "ESCOLHER",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
                color = AppColors.BrandSignalBlue
            )
        }
    }
}

private fun collectorIcon(model: CollectorModel): ImageVector {
    return when (model) {
        CollectorModel.C72 -> Icons.Outlined.Memory
        CollectorModel.R6 -> Icons.Outlined.BluetoothConnected
        CollectorModel.MC339U -> Icons.Outlined.Devices
    }
}

private fun collectorLabel(model: CollectorModel): String {
    return "COLETOR ${model.label}"
}

private fun collectorSubtitle(model: CollectorModel): String {
    return when (model) {
        CollectorModel.C72 -> "Leitor interno (UART)"
        CollectorModel.R6 -> "Leitor Bluetooth"
        CollectorModel.MC339U -> "Servico Zebra"
    }
}

@Composable
private fun StatusPill(
    text: String,
    background: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .background(background, CircleShape)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xxs)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
            color = contentColor
        )
    }
}

@Composable
private fun ReaderRecognitionDialog(
    recognized: Boolean,
    onDismiss: () -> Unit
) {
    val titleText = if (recognized) "LEITOR RECONHECIDO" else "NAO RECONHECIDO"
    val titleColor = if (recognized) AppColors.PositiveGreen else AppColors.DangerRed
    
    val supportingText = if (recognized) {
        "O leitor respondeu aos sinais e esta pronto para a operacao."
    } else {
        "O dispositivo conectou mas nao respondeu como um leitor RFID Nexus."
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(280.dp) // Tamanho fixo compacto
                .wrapContentHeight(),
            shape = AppShapes.modal,
            colors = CardDefaults.cardColors(containerColor = AppColors.DarkModal),
            border = BorderStroke(1.dp, AppColors.Divider),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(titleColor.copy(alpha = 0.1f), CircleShape)
                        .border(1.dp, titleColor.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (recognized) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        tint = titleColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.labelLarge,
                        color = titleColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }

                ActionButtonPrimary(
                    text = "FECHAR",
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(
    message: String,
    onDismissError: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.DangerSoft),
        border = BorderStroke(1.dp, AppColors.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LinkOff,
                    contentDescription = null,
                    tint = AppColors.TextPrimary
                )
                Text(
                    text = "Aviso de conexao",
                    style = MaterialTheme.typography.titleSmall,
                    color = AppColors.TextPrimary
                )
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextPrimary
            )
            ActionButtonOutline(
                text = "Fechar aviso",
                onClick = onDismissError,
                borderColor = AppColors.Divider,
                containerColor = AppColors.CardSurface,
                contentColor = AppColors.TextPrimary
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun SettingsScreenPreview() {
    NexusRFIDTheme {
        SettingsScreenPreviewContent(
            onMenuClick = {},
            selectedCollectorModel = CollectorModel.R6,
            connectionState = RfidConnectionState.Connected,
            statusMessage = MockDataSource.settingsPreviewState.statusMessage,
            connectedDevice = MockDataSource.settingsPreviewState.connectedDevice,
            soundEnabled = MockDataSource.settingsPreviewState.soundEnabled,
            isSearchingDevices = MockDataSource.settingsPreviewState.isSearchingDevices,
            availableDevices = MockDataSource.r6PreviewDevices,
            recognitionFeedback = null,
            errorMessage = null,
            onCollectorModelSelected = {},
            onSoundChange = {},
            onStartDeviceDiscovery = {},
            onStopDeviceScan = {},
            onConnectDevice = {},
            onConnectInternalCollector = {},
            onDisconnect = {},
            onDismissRecognition = {},
            onDismissError = {}
        )
    }
}
