package com.example.nexusrfid.ui.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BluetoothConnected
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.rfid.CollectorModel
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
            appState.reportError("Ative o Bluetooth para carregar os coletores pareados.")
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
            appState.reportError("Permita o Bluetooth para acessar os coletores pareados.")
        }
    }

    fun startR6Flow() {
        appState.clearErrorMessage()
        val missing = RfidPermissionGateway.missingPermissions(context)
        when {
            missing.isNotEmpty() -> permissionLauncher.launch(missing)
            !RfidPermissionGateway.isBluetoothEnabled(context) -> {
                enableBluetoothLauncher.launch(RfidPermissionGateway.bluetoothEnableIntent())
            }

            else -> appState.startR6Discovery()
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
        errorMessage = appState.errorMessage,
        onCollectorModelSelected = appState::selectCollectorModel,
        onSoundChange = appState::updateSoundEnabled,
        onStartR6Flow = ::startR6Flow,
        onStopDeviceScan = appState::stopDeviceScan,
        onConnectDevice = appState::connectToR6,
        onDisconnect = appState::disconnectReader,
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
    errorMessage: String?,
    onCollectorModelSelected: (CollectorModel) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onStartR6Flow: () -> Unit,
    onStopDeviceScan: () -> Unit,
    onConnectDevice: (RfidDevice) -> Unit,
    onDisconnect: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val r6Ready = selectedCollectorModel == CollectorModel.R6
    val actionLabel = when {
        isSearchingDevices -> "Carregando lista"
        availableDevices.isEmpty() -> "Mostrar pareados"
        else -> "Atualizar lista"
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
                    title = "Modelo em uso"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                    ) {
                        CollectorOptionButton(
                            label = "C72",
                            icon = Icons.Outlined.Memory,
                            selected = selectedCollectorModel == CollectorModel.C72,
                            modifier = Modifier.weight(1f),
                            onClick = { onCollectorModelSelected(CollectorModel.C72) }
                        )
                        CollectorOptionButton(
                            label = "R6",
                            icon = Icons.Outlined.BluetoothConnected,
                            selected = selectedCollectorModel == CollectorModel.R6,
                            modifier = Modifier.weight(1f),
                            onClick = { onCollectorModelSelected(CollectorModel.R6) }
                        )
                    }
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

            if (r6Ready) {
                item {
                    SettingsSectionCard(
                        eyebrow = "BLUETOOTH",
                        title = "Coletores pareados"
                    ) {
                        Text(
                            text = "O app lista somente dispositivos ja pareados no Android. Assim a escolha fica mais segura e direta.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                        ) {
                            ActionButtonPrimary(
                                text = actionLabel,
                                onClick = if (isSearchingDevices) onStopDeviceScan else onStartR6Flow,
                                modifier = Modifier.weight(1f),
                                enabled = !isSearchingDevices
                            )

                            if (connectionState == RfidConnectionState.Connected) {
                                ActionButtonOutline(
                                    text = "Desconectar",
                                    onClick = onDisconnect,
                                    modifier = Modifier.weight(1f),
                                    borderColor = AppColors.Divider,
                                    containerColor = AppColors.FieldBackground
                                )
                            }
                        }
                    }
                }

                item {
                    if (availableDevices.isEmpty()) {
                        SettingsSectionCard(
                            eyebrow = "AGUARDANDO",
                            title = "Nenhum coletor pronto"
                        ) {
                            Text(
                                text = "Pareie o R6 nas configuracoes do Android e volte aqui para conectar.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TextSecondary
                            )
                        }
                    } else {
                        SettingsSectionCard(
                            eyebrow = "${availableDevices.size} PAREADO(S)",
                            title = "Lista de conexao"
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                            ) {
                                availableDevices.forEach { device ->
                                    DeviceRow(
                                        device = device,
                                        connected = connectedDevice?.address == device.address,
                                        onConnect = { onConnectDevice(device) }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    SettingsSectionCard(
                        eyebrow = "C72",
                        title = "Fluxo fisico"
                    ) {
                        Text(
                            text = "O C72 continua plug and play, mas a conexao fisica sera tratada na etapa especifica dele.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )
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
                        text = if (selectedCollectorModel == CollectorModel.R6) {
                            "Conexao pronta para o R6"
                        } else {
                            "C72 selecionado"
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
                        imageVector = if (selectedCollectorModel == CollectorModel.R6) {
                            Icons.Outlined.BluetoothConnected
                        } else {
                            Icons.Outlined.Memory
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
private fun CollectorOptionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = AppShapes.button,
        border = BorderStroke(
            1.dp,
            if (selected) AppColors.PrimaryActionBlue else AppColors.Divider
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) AppColors.AccentSurface else AppColors.FieldBackground,
            contentColor = if (selected) AppColors.TopBarBlue else AppColors.TextPrimary
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = AppSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun DeviceRow(
    device: RfidDevice,
    connected: Boolean,
    onConnect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AppColors.Divider, AppShapes.card)
            .background(
                if (connected) AppColors.AccentSurface else AppColors.FieldBackground,
                AppShapes.card
            )
            .padding(AppSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(AppColors.TopBarBlue.copy(alpha = 0.08f), CircleShape)
                .padding(AppSpacing.sm),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Devices,
                contentDescription = null,
                tint = AppColors.TopBarBlue
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            Text(
                text = device.displayName,
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TextPrimary
            )
            Text(
                text = device.address,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                color = AppColors.TextSecondary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                StatusPill(
                    text = "Pareado",
                    background = AppColors.TopBarBlue.copy(alpha = 0.08f),
                    contentColor = AppColors.TopBarBlue
                )
                if (connected) {
                    StatusPill(
                        text = "Conectado",
                        background = AppColors.PositiveGreen.copy(alpha = 0.14f),
                        contentColor = AppColors.PositiveGreen
                    )
                }
            }
        }

        Button(
            onClick = onConnect,
            enabled = !connected,
            shape = AppShapes.button,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.PrimaryActionBlue,
                contentColor = AppColors.TopBarOnBlue
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(if (connected) "Ativo" else "Conectar")
        }
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
            errorMessage = null,
            onCollectorModelSelected = {},
            onSoundChange = {},
            onStartR6Flow = {},
            onStopDeviceScan = {},
            onConnectDevice = {},
            onDisconnect = {},
            onDismissError = {}
        )
    }
}
