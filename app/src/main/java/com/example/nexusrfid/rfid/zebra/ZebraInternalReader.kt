package com.example.nexusrfid.rfid.zebra

import android.content.Context
import android.util.Log
import com.example.nexusrfid.rfid.RfidConnectionState
import com.example.nexusrfid.rfid.RfidDevice
import com.example.nexusrfid.rfid.RfidReader
import com.example.nexusrfid.rfid.RfidTagRead
import com.zebra.rfid.api3.*
import java.util.concurrent.ConcurrentLinkedQueue

class ZebraInternalReader(
    context: Context
) : RfidReader, RfidEventsListener {
    private val appContext = context.applicationContext
    private var readers: Readers? = null
    private var reader: RFIDReader? = null
    private var statusListener: ((RfidConnectionState, RfidDevice?) -> Unit)? = null
    
    private val tagBuffer = ConcurrentLinkedQueue<RfidTagRead>()

    init {
        runCatching {
            // Para MC3300x/MC3390x internos
            readers = Readers(appContext, ENUM_TRANSPORT.SERVICE_SERIAL)
        }.onFailure {
            Log.e("ZebraReader", "Falha ao iniciar Readers Zebra", it)
        }
    }

    override fun setStatusListener(listener: (RfidConnectionState, RfidDevice?) -> Unit) {
        statusListener = listener
    }

    override fun startScan(onDeviceFound: (RfidDevice) -> Unit) {
        runCatching {
            val availableReaders = readers?.GetAvailableRFIDReaderList() ?: emptyList()
            if (availableReaders.isEmpty()) {
                statusListener?.invoke(RfidConnectionState.Error, null)
                return
            }
            
            availableReaders.forEach { device ->
                onDeviceFound(RfidDevice(name = device.name, address = device.address))
            }
            statusListener?.invoke(RfidConnectionState.Idle, null)
        }.onFailure {
            statusListener?.invoke(RfidConnectionState.Error, null)
        }
    }

    override fun stopScan() {
    }

    override fun connect(address: String) {
        statusListener?.invoke(RfidConnectionState.Connecting, RfidDevice("MC339U", address))
        
        runCatching {
            val deviceList = readers?.GetAvailableRFIDReaderList()
            val readerDevice = deviceList?.find { it.address == address } ?: deviceList?.firstOrNull()
            
            if (readerDevice == null) {
                statusListener?.invoke(RfidConnectionState.Error, null)
                return
            }

            reader = readerDevice.rfidReader
            reader?.connect()
            
            if (reader?.isConnected == true) {
                setupReader()
                statusListener?.invoke(RfidConnectionState.Connected, RfidDevice(readerDevice.name, readerDevice.address))
            } else {
                statusListener?.invoke(RfidConnectionState.Error, null)
            }
        }.onFailure {
            Log.e("ZebraReader", "Erro ao conectar Zebra", it)
            statusListener?.invoke(RfidConnectionState.Error, null)
        }
    }

    private fun setupReader() {
        runCatching {
            reader?.let { r ->
                r.Events.addEventsListener(this)
                r.Events.setHandheldEvent(true)
                r.Events.setTagReadEvent(true)
                r.Events.setReaderDisconnectEvent(true)
                
                // Configuração de Gatilho
                r.Config.startTrigger = TriggerInfo().StartTrigger.apply {
                    setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE)
                }
                r.Config.stopTrigger = TriggerInfo().StopTrigger.apply {
                    setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE)
                }
                
                // Modo RFID para o gatilho
                r.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true)
            }
        }.onFailure {
            Log.e("ZebraReader", "Erro ao configurar eventos Zebra", it)
        }
    }

    override fun disconnect() {
        runCatching {
            reader?.Events?.removeEventsListener(this)
            reader?.disconnect()
        }
        statusListener?.invoke(RfidConnectionState.Disconnected, null)
    }

    override fun setPower(power: Int): Boolean {
        return runCatching {
            val config = reader?.Config?.Antennas?.getAntennaRfConfig(1)
            // Zebra MC3300 escala 100-300
            config?.transmitPowerIndex = (power * 10).coerceIn(100, 300)
            reader?.Config?.Antennas?.setAntennaRfConfig(1, config)
            true
        }.getOrDefault(false)
    }

    override fun startInventory(): Boolean {
        tagBuffer.clear()
        return runCatching {
            reader?.Actions?.Inventory?.perform()
            true
        }.getOrDefault(false)
    }

    override fun stopInventory(): Boolean {
        return runCatching {
            reader?.Actions?.Inventory?.stop()
            true
        }.getOrDefault(false)
    }

    override fun readAvailableTags(): List<RfidTagRead> {
        val tags = mutableListOf<RfidTagRead>()
        while (tagBuffer.isNotEmpty()) {
            tagBuffer.poll()?.let { tags.add(it) }
        }
        return tags
    }

    override fun release() {
        disconnect()
        runCatching { readers?.Dispose() }
    }

    override fun eventReadNotify(e: RfidReadEvents?) {
        val readTags = reader?.Actions?.getReadTags(100)
        readTags?.forEach { tag ->
            tagBuffer.add(
                RfidTagRead(
                    epc = tag.tagID,
                    rssi = tag.peakRSSI.toInt(),
                    seenAtMillis = System.currentTimeMillis()
                )
            )
        }
    }

    override fun eventStatusNotify(e: RfidStatusEvents?) {
        val eventData = e?.StatusEventData
        if (eventData?.statusEventType == STATUS_EVENT_TYPE.DISCONNECTION_EVENT) {
            statusListener?.invoke(RfidConnectionState.Disconnected, null)
        }
    }
}
