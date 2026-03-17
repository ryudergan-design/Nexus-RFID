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
import com.example.nexusrfid.rfid.c72.C72InternalReader
import com.example.nexusrfid.rfid.r6.R6BluetoothReader
import com.example.nexusrfid.rfid.zebra.ZebraInternalReader

@Stable
class NexusRfidAppState internal constructor(
    context: Context,
    private val r6Reader: RfidReader = R6BluetoothReader(context.applicationContext),
    private val c72Reader: RfidReader = C72InternalReader(context.applicationContext),
    private val zebraReader: RfidReader = ZebraInternalReader(context.applicationContext)
) {
    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
    private var lastToneAt = 0L

    var selectedCollectorModel by mutableStateOf(CollectorModel.R6)
        private set

    private val currentReader: RfidReader
        get() = when (selectedCollectorModel) {
            CollectorModel.R6 -> r6Reader
            CollectorModel.C72 -> c72Reader
            CollectorModel.MC339U -> zebraReader
        }

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

    var statusMessage by mutableStateOf("Selecione o coletor e conecte para iniciar.")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isDeviceScanRunning by mutableStateOf(false)
        private set

    val availableDevices = mutableStateListOf<RfidDevice>()

    init {
        setupReaderListeners()
    }

    private fun setupReaderListeners() {
        val listener: (RfidConnectionState, RfidDevice?) -> Unit = { state, device ->
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
                
                handleConnectionStateChange(state, device)
            }
        }
        
        r6Reader.setStatusListener(listener)
        c72Reader.setStatusListener(listener)
        zebraReader.setStatusListener(listener)
    }

    private fun handleConnectionStateChange(state: RfidConnectionState, device: RfidDevice?) {
        when (state) {
            RfidConnectionState.Connected -> {
                val recognized = currentReader.setPower(readerPower)
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
                errorMessage = null
            }
            RfidConnectionState.Error -> {
                readerRecognitionState = ReaderRecognitionState.Unknown
                recognitionFeedback = null
                errorMessage = connectionErrorMessage()
            }
            else -> {
                readerRecognitionState = ReaderRecognitionState.Unknown
            }
        }
    }

    fun selectCollectorModel(model: CollectorModel) {
        if (selectedCollectorModel == model) return

        stopInventory()
        stopDeviceScan()
        currentReader.disconnect()

        selectedCollectorModel = model
        connectedDevice = null
        readerRecognitionState = ReaderRecognitionState.Unknown
        recognitionFeedback = null
        availableDevices.clear()
        connectionState = RfidConnectionState.Idle
        errorMessage = null
        
        statusMessage = when (model) {
            CollectorModel.C72 -> "C72 selecionado. Toque em Conectar para abrir o barramento interno."
            CollectorModel.R6 -> "R6 selecionado. Busque o leitor ativo para conectar."
            CollectorModel.MC339U -> "MC339U selecionado. Toque em Conectar para abrir o servico Zebra."
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        soundEnabled = enabled
    }

    fun updateReaderPower(power: Int) {
        val clampedPower = power.coerceIn(1, 30)
        readerPower = clampedPower

        if (connectionState == RfidConnectionState.Connected) {
            if (readerRecognitionState != ReaderRecognitionState.Recognized) {
                errorMessage = "Leitor nao reconhecido. Selecione um leitor valido."
                return
            }
            val applied = currentReader.setPower(clampedPower)
            if (!applied) {
                errorMessage = "Nao foi possivel aplicar a potencia agora."
            }
        }
    }

    fun startR6Discovery() {
        errorMessage = null
        availableDevices.clear()
        
        when (selectedCollectorModel) {
            CollectorModel.C72 -> {
                val c72Internal = RfidDevice(name = "LEITOR C72", address = "UART")
                availableDevices.add(c72Internal)
                statusMessage = "Leitor interno C72 localizado."
                return
            }
            CollectorModel.MC339U -> {
                statusMessage = "Buscando servicos Zebra..."
                isDeviceScanRunning = true
                zebraReader.startScan { device ->
                    mainHandler.post {
                        availableDevices.add(device)
                        isDeviceScanRunning = false
                    }
                }
                return
            }
            else -> {
                isDeviceScanRunning = true
                if (connectionState != RfidConnectionState.Connected) {
                    connectionState = RfidConnectionState.Scanning
                    statusMessage = "Buscando leitores Bluetooth ativos..."
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
        }
    }

    fun stopDeviceScan() {
        currentReader.stopScan()
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
        errorMessage = null
        stopDeviceScan()
        
        if (connectedDevice?.address != null && connectedDevice?.address != device.address) {
            currentReader.disconnect()
        }
        
        readerRecognitionState = ReaderRecognitionState.Checking
        recognitionFeedback = null
        statusMessage = "Conectando em ${device.displayName}..."
        currentReader.connect(device.address)
    }

    fun connectInternalCollector() {
        if (selectedCollectorModel != CollectorModel.C72) return

        errorMessage = null
        stopDeviceScan()
        val internalDevice = RfidDevice(name = "LEITOR C72", address = "UART")
        readerRecognitionState = ReaderRecognitionState.Checking
        recognitionFeedback = null
        statusMessage = "Conectando em ${internalDevice.displayName}..."
        currentReader.connect(internalDevice.address)
    }

    fun disconnectReader() {
        stopInventory()
        stopDeviceScan()
        currentReader.disconnect()
        connectedDevice = null
        connectionState = RfidConnectionState.Disconnected
        readerRecognitionState = ReaderRecognitionState.Unknown
        recognitionFeedback = null
        statusMessage = "Coletor desconectado."
    }

    fun startInventory(): Boolean {
        if (connectionState != RfidConnectionState.Connected) {
            errorMessage = "Conecte o coletor antes de iniciar a busca."
            return false
        }

        if (readerRecognitionState != ReaderRecognitionState.Recognized) {
            errorMessage = "Leitor nao reconhecido. Selecione um leitor valido."
            return false
        }

        currentReader.setPower(readerPower)
        val started = currentReader.startInventory()
        if (started) {
            errorMessage = null
            statusMessage = "Buscando tags com o ${selectedCollectorModel.label}..."
        } else {
            errorMessage = "Nao foi possivel iniciar a busca RFID."
        }
        return started
    }

    fun stopInventory(): Boolean {
        val stopped = currentReader.stopInventory()
        if (stopped && connectionState == RfidConnectionState.Connected) {
            statusMessage = "Busca RFID parada."
        }
        return stopped
    }

    fun readTagBatch(): List<RfidTagRead> {
        return if (connectionState == RfidConnectionState.Connected &&
            readerRecognitionState == ReaderRecognitionState.Recognized
        ) {
            currentReader.readAvailableTags()
        } else {
            emptyList()
        }
    }

    fun playDetectionTone() {
        if (!soundEnabled) return

        val now = SystemClock.elapsedRealtime()
        // Intervalo reduzido para 200ms para feedback mais rapido
        if (now - lastToneAt < 200L) return

        lastToneAt = now
        // TONE_PROP_BEEP e mais curto e agudo, ideal para RFID
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
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
        runCatching { c72Reader.release() }
        runCatching { zebraReader.release() }
        runCatching { toneGenerator.release() }
    }

    private fun RfidConnectionState.toStatusMessage(device: RfidDevice?): String {
        val modelLabel = selectedCollectorModel.label
        return when (this) {
            RfidConnectionState.Idle -> "Selecione o coletor e conecte para iniciar."
            RfidConnectionState.Scanning -> "Buscando leitores ativos..."
            RfidConnectionState.Connecting -> "Conectando em ${device?.displayName ?: modelLabel}..."
            RfidConnectionState.Connected -> "${device?.displayName ?: modelLabel} conectado."
            RfidConnectionState.Disconnected -> "Coletor desconectado."
            RfidConnectionState.Error -> "Nao foi possivel falar com o coletor agora."
        }
    }

    private fun connectionErrorMessage(): String {
        return when (selectedCollectorModel) {
            CollectorModel.C72 ->
                "Falha ao iniciar o leitor interno C72. Verifique se o coletor esta com o leitor habilitado e tente novamente."
            CollectorModel.R6 ->
                "Falha ao conectar ao leitor Bluetooth. Verifique se ele esta ligado e pareado."
            CollectorModel.MC339U ->
                "Falha ao iniciar o servico Zebra. Verifique se o leitor esta habilitado."
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
