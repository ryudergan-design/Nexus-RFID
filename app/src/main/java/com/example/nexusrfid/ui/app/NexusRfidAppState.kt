package com.example.nexusrfid.ui.app

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.nexusrfid.rfid.CollectorModel
import com.example.nexusrfid.rfid.ReaderRecognitionFeedback
import com.example.nexusrfid.rfid.ReaderRecognitionState
import com.example.nexusrfid.rfid.RfidConnectionState
import com.example.nexusrfid.rfid.RfidDevice
import com.example.nexusrfid.rfid.RfidReader
import com.example.nexusrfid.rfid.RfidTagRead
import com.example.nexusrfid.rfid.r6.R6BluetoothReader

@Stable
class NexusRfidAppState internal constructor(
    context: Context,
    private val r6Reader: RfidReader = R6BluetoothReader(context.applicationContext)
) {
    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
    private var lastToneAt = 0L

    var selectedCollectorModel by mutableStateOf(CollectorModel.R6)
        private set

    var soundEnabled by mutableStateOf(true)
        private set

    var readerPower by mutableIntStateOf(15)
        private set

    var connectionState by mutableStateOf(RfidConnectionState.Idle)
        private set

    var connectedDevice by mutableStateOf<RfidDevice?>(null)
        private set

    var readerRecognitionState by mutableStateOf(ReaderRecognitionState.Unknown)
        private set

    var recognitionFeedback by mutableStateOf<ReaderRecognitionFeedback?>(null)
        private set

    var statusMessage by mutableStateOf("Selecione o coletor e conecte o R6 para iniciar.")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isDeviceScanRunning by mutableStateOf(false)
        private set

    val availableDevices = mutableStateListOf<RfidDevice>()

    init {
        r6Reader.setStatusListener { state, device ->
            mainHandler.post {
                val wasConnected = connectionState == RfidConnectionState.Connected
                if (wasConnected && (state == RfidConnectionState.Scanning || state == RfidConnectionState.Idle)) {
                    isDeviceScanRunning = state == RfidConnectionState.Scanning
                    return@post
                }
                connectionState = state
                connectedDevice = device
                isDeviceScanRunning = state == RfidConnectionState.Scanning
                statusMessage = state.toStatusMessage(device)
                when (state) {
                    RfidConnectionState.Connected -> {
                        val recognized = r6Reader.setPower(readerPower)
                        readerRecognitionState = if (recognized) {
                            ReaderRecognitionState.Recognized
                        } else {
                            ReaderRecognitionState.NotRecognized
                        }
                        recognitionFeedback = ReaderRecognitionFeedback(
                            recognized = recognized,
                            deviceName = device?.displayName
                        )
                        errorMessage = if (recognized) {
                            null
                        } else {
                            "Nao foi possivel reconhecer o leitor selecionado."
                        }
                    }
                    RfidConnectionState.Connecting -> {
                        readerRecognitionState = ReaderRecognitionState.Checking
                    }
                    else -> {
                        readerRecognitionState = ReaderRecognitionState.Unknown
                    }
                }
            }
        }
    }

    fun selectCollectorModel(model: CollectorModel) {
        if (selectedCollectorModel == model) return

        stopDeviceScan()
        stopInventory()

        if (selectedCollectorModel == CollectorModel.R6) {
            r6Reader.disconnect()
        }

        selectedCollectorModel = model
        connectedDevice = null
        readerRecognitionState = ReaderRecognitionState.Unknown
        recognitionFeedback = null
        availableDevices.clear()
        connectionState = RfidConnectionState.Idle
        errorMessage = null
        statusMessage = when (model) {
            CollectorModel.C72 -> "C72 selecionado. A conexao fisica entra em uma etapa posterior."
            CollectorModel.R6 -> "R6 selecionado. Busque o leitor ativo para conectar."
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        soundEnabled = enabled
    }

    fun updateReaderPower(power: Int) {
        val clampedPower = power.coerceIn(1, 30)
        readerPower = clampedPower

        if (selectedCollectorModel == CollectorModel.R6 &&
            connectionState == RfidConnectionState.Connected
        ) {
            if (readerRecognitionState != ReaderRecognitionState.Recognized) {
                errorMessage = "Leitor nao reconhecido. Selecione um leitor valido."
                return
            }
            val applied = r6Reader.setPower(clampedPower)
            if (!applied) {
                errorMessage = "Nao foi possivel aplicar a potencia agora."
            }
        }
    }

    fun startR6Discovery() {
        if (selectedCollectorModel != CollectorModel.R6) {
            errorMessage = "Selecione o R6 para usar Bluetooth nesta etapa."
            return
        }

        errorMessage = null
        availableDevices.clear()
        isDeviceScanRunning = true
        if (connectionState != RfidConnectionState.Connected) {
            connectionState = RfidConnectionState.Scanning
            statusMessage = "Buscando leitores ativos..."
        }

        r6Reader.startScan { device ->
            mainHandler.post {
                val index = availableDevices.indexOfFirst { it.address == device.address }
                if (index >= 0) {
                    availableDevices[index] = device
                } else {
                    availableDevices.add(device)
                }
            }
        }
    }

    fun stopDeviceScan() {
        r6Reader.stopScan()
        isDeviceScanRunning = false
        if (connectionState == RfidConnectionState.Scanning) {
            connectionState = RfidConnectionState.Idle
            statusMessage = when {
                availableDevices.isEmpty() ->
                    "Nenhum leitor ativo encontrado. Ligue o leitor e tente novamente."
                availableDevices.size == 1 -> "1 leitor ativo pronto para conectar."
                else -> "${availableDevices.size} leitores ativos prontos para conectar."
            }
        }
    }

    fun connectToR6(device: RfidDevice) {
        if (selectedCollectorModel != CollectorModel.R6) {
            errorMessage = "Selecione o R6 antes de conectar."
            return
        }

        errorMessage = null
        stopDeviceScan()
        if (connectedDevice?.address != null && connectedDevice?.address != device.address) {
            r6Reader.disconnect()
        }
        readerRecognitionState = ReaderRecognitionState.Checking
        recognitionFeedback = null
        statusMessage = "Conectando em ${device.displayName}..."
        r6Reader.connect(device.address)
    }

    fun disconnectReader() {
        stopInventory()
        stopDeviceScan()
        r6Reader.disconnect()
        connectedDevice = null
        connectionState = RfidConnectionState.Disconnected
        readerRecognitionState = ReaderRecognitionState.Unknown
        recognitionFeedback = null
        statusMessage = "Coletor desconectado. Abra a lista de leitores ativos para reconectar."
    }

    fun startInventory(): Boolean {
        if (selectedCollectorModel != CollectorModel.R6) {
            errorMessage = "O primeiro teste real desta fase esta disponivel apenas para o R6."
            return false
        }

        if (connectionState != RfidConnectionState.Connected) {
            errorMessage = "Conecte o R6 antes de iniciar a busca."
            return false
        }

        if (readerRecognitionState != ReaderRecognitionState.Recognized) {
            errorMessage = "Leitor nao reconhecido. Selecione um leitor valido."
            return false
        }

        r6Reader.setPower(readerPower)
        val started = r6Reader.startInventory()
        if (started) {
            errorMessage = null
            statusMessage = "Buscando tags com o R6..."
        } else {
            errorMessage = "Nao foi possivel iniciar a busca RFID."
        }
        return started
    }

    fun stopInventory(): Boolean {
        val stopped = r6Reader.stopInventory()
        if (stopped && connectionState == RfidConnectionState.Connected) {
            statusMessage = "Busca RFID parada."
        }
        return stopped
    }

    fun readTagBatch(): List<RfidTagRead> {
        return if (selectedCollectorModel == CollectorModel.R6 &&
            connectionState == RfidConnectionState.Connected &&
            readerRecognitionState == ReaderRecognitionState.Recognized
        ) {
            r6Reader.readAvailableTags()
        } else {
            emptyList()
        }
    }

    fun playDetectionTone() {
        if (!soundEnabled) return

        val now = SystemClock.elapsedRealtime()
        if (now - lastToneAt < 450L) return

        lastToneAt = now
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 120)
    }

    fun clearRecognitionFeedback() {
        recognitionFeedback = null
    }

    fun clearErrorMessage() {
        errorMessage = null
    }

    fun reportError(message: String) {
        errorMessage = message
    }

    fun dispose() {
        runCatching { stopInventory() }
        runCatching { stopDeviceScan() }
        runCatching { r6Reader.release() }
        runCatching { toneGenerator.release() }
    }

    private fun RfidConnectionState.toStatusMessage(device: RfidDevice?): String {
        return when (this) {
            RfidConnectionState.Idle -> "Selecione o coletor e conecte o R6 para iniciar."
            RfidConnectionState.Scanning -> "Buscando leitores ativos..."
            RfidConnectionState.Connecting -> "Conectando em ${device?.displayName ?: "R6"}..."
            RfidConnectionState.Connected -> "${device?.displayName ?: "R6"} conectado."
            RfidConnectionState.Disconnected -> "Coletor desconectado."
            RfidConnectionState.Error -> "Nao foi possivel falar com o coletor agora."
        }
    }
}

@Composable
fun rememberNexusRfidAppState(): NexusRfidAppState {
    val context = LocalContext.current.applicationContext
    return remember(context) {
        NexusRfidAppState(context)
    }
}
