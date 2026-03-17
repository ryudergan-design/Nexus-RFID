package com.example.nexusrfid.ui.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            appState.reportError("Ative o Bluetooth para conectar o R6.")
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
            appState.reportError("Permita o Bluetooth para procurar o R6.")
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.ScreenBackground,
        topBar = {
            AppTopBar(
                title = "Configuracoes",
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
                SettingsCard(title = "Coletor") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                    ) {
                        CollectorOptionButton(
                            label = "C72",
                            selected = selectedCollectorModel == CollectorModel.C72,
                            modifier = Modifier.weight(1f),
                            onClick = { onCollectorModelSelected(CollectorModel.C72) }
                        )
                        CollectorOptionButton(
                            label = "R6",
                            selected = selectedCollectorModel == CollectorModel.R6,
                            modifier = Modifier.weight(1f),
                            onClick = { onCollectorModelSelected(CollectorModel.R6) }
                        )
                    }
                }
            }

            item {
                SettingsCard(title = "Status") {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
                    ) {
                        Text(
                            text = statusMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.TextPrimary
                        )
                        if (connectedDevice != null) {
                            Text(
                                text = "${connectedDevice.displayName}  |  ${connectedDevice.address}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TextSecondary
                            )
                        }
                    }
                }
            }

            item {
                SettingsCard(title = "Som de deteccao") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
                        ) {
                            Text(
                                text = if (soundEnabled) "Som ligado" else "Som desligado",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppColors.TextPrimary
                            )
                            Text(
                                text = "Use o alarme sonoro so quando ele ajudar na localizacao.",
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

            if (selectedCollectorModel == CollectorModel.R6) {
                item {
                    SettingsCard(title = "Bluetooth do R6") {
                        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                            when {
                                connectionState == RfidConnectionState.Connected -> {
                                    ActionButtonOutline(
                                        text = "Desconectar coletor",
                                        onClick = onDisconnect,
                                        borderColor = AppColors.Divider,
                                        containerColor = AppColors.FieldBackground
                                    )
                                }

                                isSearchingDevices -> {
                                    ActionButtonPrimary(
                                        text = "Parar busca",
                                        onClick = onStopDeviceScan
                                    )
                                }

                                else -> {
                                    ActionButtonPrimary(
                                        text = "Procurar coletor",
                                        onClick = onStartR6Flow
                                    )
                                }
                            }

                            Text(
                                text = "Fluxo plug and play: escolha o R6, permita o Bluetooth e selecione o coletor.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.TextSecondary
                            )
                        }
                    }
                }

                if (availableDevices.isNotEmpty()) {
                    item {
                        SettingsCard(title = "Coletores encontrados") {
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
                    SettingsCard(title = "C72") {
                        Text(
                            text = "Nesta etapa, o C72 fica apenas preparado no fluxo. A conexao fisica entra depois.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )
                    }
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                item {
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
                                .padding(AppSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                        ) {
                            Text(
                                text = errorMessage,
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
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    content: @Composable () -> Unit
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
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TextPrimary
            )
            content()
        }
    }
}

@Composable
private fun CollectorOptionButton(
    label: String,
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
            if (selected) AppColors.BrandSignalBlue.copy(alpha = 0.42f) else AppColors.Divider
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) AppColors.AccentSurface else AppColors.FieldBackground,
            contentColor = if (selected) AppColors.TopBarBlue else AppColors.TextPrimary
        )
    ) {
        Text(text = label)
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
            .padding(AppSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)
        ) {
            Text(
                text = device.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextPrimary
            )
            Text(
                text = buildString {
                    append(device.address)
                    if (device.rssi != null) {
                        append("  |  RSSI ${device.rssi}")
                    }
                    if (device.bonded) {
                        append("  |  pareado")
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextSecondary
            )
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
            Text(if (connected) "Conectado" else "Conectar")
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
