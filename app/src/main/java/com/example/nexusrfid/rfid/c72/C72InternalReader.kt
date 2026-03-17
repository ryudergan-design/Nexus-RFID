package com.example.nexusrfid.rfid.c72

import android.content.Context
import com.example.nexusrfid.rfid.RfidConnectionState
import com.example.nexusrfid.rfid.RfidDevice
import com.example.nexusrfid.rfid.RfidReader
import com.example.nexusrfid.rfid.RfidTagRead
import com.rscja.deviceapi.RFIDWithUHFUART

class C72InternalReader(
    context: Context
) : RfidReader {
    private val appContext = context.applicationContext
    private var reader: RFIDWithUHFUART? = null
    private var statusListener: ((RfidConnectionState, RfidDevice?) -> Unit)? = null

    init {
        runCatching {
            reader = RFIDWithUHFUART.getInstance()
        }
    }

    override fun setStatusListener(listener: (RfidConnectionState, RfidDevice?) -> Unit) {
        statusListener = listener
    }

    override fun startScan(onDeviceFound: (RfidDevice) -> Unit) {
        // C72 não escaneia dispositivos externos, ele é o próprio dispositivo.
        val localDevice = RfidDevice(name = "C72 Interno", address = "UART")
        onDeviceFound(localDevice)
        statusListener?.invoke(RfidConnectionState.Idle, localDevice)
    }

    override fun stopScan() {
        // Nada a fazer
    }

    override fun connect(address: String) {
        statusListener?.invoke(RfidConnectionState.Connecting, RfidDevice("C72", "UART"))
        
        val success = runCatching {
            reader?.init() ?: false
        }.getOrDefault(false)

        if (success) {
            statusListener?.invoke(RfidConnectionState.Connected, RfidDevice("C72", "UART"))
        } else {
            statusListener?.invoke(RfidConnectionState.Error, null)
        }
    }

    override fun disconnect() {
        runCatching { reader?.free() }
        statusListener?.invoke(RfidConnectionState.Disconnected, null)
    }

    override fun setPower(power: Int): Boolean {
        return runCatching {
            reader?.setPower(power.coerceIn(1, 30)) ?: false
        }.getOrDefault(false)
    }

    override fun startInventory(): Boolean {
        return runCatching { reader?.startInventoryTag() ?: false }.getOrDefault(false)
    }

    override fun stopInventory(): Boolean {
        return runCatching { reader?.stopInventory() ?: false }.getOrDefault(false)
    }

    override fun readAvailableTags(): List<RfidTagRead> {
        return runCatching {
            val tags = mutableListOf<RfidTagRead>()
            var info = reader?.readTagFromBuffer()
            while (info != null) {
                tags.add(
                    RfidTagRead(
                        epc = info.epc.orEmpty(),
                        rssi = info.rssi?.toIntOrNull(),
                        tid = info.tid,
                        seenAtMillis = System.currentTimeMillis()
                    )
                )
                info = reader?.readTagFromBuffer()
            }
            tags
        }.getOrDefault(emptyList())
    }

    override fun release() {
        runCatching { reader?.stopInventory() }
        runCatching { reader?.free() }
    }
}
