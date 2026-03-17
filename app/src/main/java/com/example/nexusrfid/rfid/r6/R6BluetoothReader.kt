package com.example.nexusrfid.rfid.r6

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.example.nexusrfid.rfid.RfidConnectionState
import com.example.nexusrfid.rfid.RfidDevice
import com.example.nexusrfid.rfid.RfidReader
import com.example.nexusrfid.rfid.RfidTagRead
import com.rscja.deviceapi.RFIDWithUHFBluetooth

class R6BluetoothReader(
    context: Context
) : RfidReader {
    private val appContext = context.applicationContext
    private val reader = RFIDWithUHFBluetooth.getInstance()

    private var statusListener: ((RfidConnectionState, RfidDevice?) -> Unit)? = null

    private val statusCallback = RFIDWithUHFBluetooth.BTStatusCallback { status, device ->
        statusListener?.invoke(status.toUiState(), device?.toRfidDevice())
    }

    init {
        reader.init(appContext)
        reader.setStatusCallback(statusCallback)
    }

    override fun setStatusListener(listener: (RfidConnectionState, RfidDevice?) -> Unit) {
        statusListener = listener
    }

    override fun startScan(onDeviceFound: (RfidDevice) -> Unit) {
        statusListener?.invoke(RfidConnectionState.Scanning, null)
        reader.stopScanBTDevices()
        reader.scanBTDevices { bluetoothDevice, rssi, _ ->
            if (bluetoothDevice != null) {
                onDeviceFound(bluetoothDevice.toRfidDevice(rssi))
            }
        }
    }

    override fun stopScan() {
        reader.stopScanBTDevices()
        if (reader.getConnectStatus() != RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
            statusListener?.invoke(RfidConnectionState.Idle, null)
        }
    }

    override fun connect(address: String) {
        val pendingDevice = RfidDevice(
            name = "R6",
            address = address
        )
        statusListener?.invoke(RfidConnectionState.Connecting, pendingDevice)
        reader.connect(address, statusCallback)
    }

    override fun disconnect() {
        reader.disconnect()
        statusListener?.invoke(RfidConnectionState.Disconnected, null)
    }

    override fun setPower(power: Int): Boolean {
        return runCatching {
            reader.setPower(power.coerceIn(1, 30))
        }.getOrDefault(false)
    }

    override fun startInventory(): Boolean {
        return runCatching { reader.startInventoryTag() }.getOrDefault(false)
    }

    override fun stopInventory(): Boolean {
        return runCatching { reader.stopInventoryTag() }.getOrDefault(false)
    }

    override fun readAvailableTags(): List<RfidTagRead> {
        return runCatching {
            reader.readTagFromBuffer()
                ?.map { info ->
                    RfidTagRead(
                        epc = info.epc.orEmpty(),
                        rssi = info.rssi.extractRssiValue(),
                        tid = info.tid,
                        seenAtMillis = System.currentTimeMillis()
                    )
                }
                .orEmpty()
        }.getOrDefault(emptyList())
    }

    override fun release() {
        runCatching { reader.stopInventoryTag() }
        runCatching { reader.stopScanBTDevices() }
        runCatching { reader.disconnect() }
        runCatching { reader.free() }
    }

    private fun RFIDWithUHFBluetooth.StatusEnum.toUiState(): RfidConnectionState {
        return when (this) {
            RFIDWithUHFBluetooth.StatusEnum.CONNECTED -> RfidConnectionState.Connected
            RFIDWithUHFBluetooth.StatusEnum.CONNECTING -> RfidConnectionState.Connecting
            RFIDWithUHFBluetooth.StatusEnum.DISCONNECTED -> RfidConnectionState.Disconnected
        }
    }

    private fun BluetoothDevice.toRfidDevice(rssi: Int? = null): RfidDevice {
        return RfidDevice(
            name = name.orEmpty(),
            address = address.orEmpty(),
            rssi = rssi,
            bonded = bondState == BluetoothDevice.BOND_BONDED
        )
    }

    private fun String?.extractRssiValue(): Int? {
        if (this.isNullOrBlank()) return null
        return Regex("-?\\d+").find(this)?.value?.toIntOrNull()
    }
}
