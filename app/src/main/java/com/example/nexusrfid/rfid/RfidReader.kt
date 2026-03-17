package com.example.nexusrfid.rfid

interface RfidReader {
    fun setStatusListener(listener: (RfidConnectionState, RfidDevice?) -> Unit)

    fun startScan(onDeviceFound: (RfidDevice) -> Unit)

    fun stopScan()

    fun connect(address: String)

    fun disconnect()

    fun setPower(power: Int): Boolean

    fun startInventory(): Boolean

    fun stopInventory(): Boolean

    fun readAvailableTags(): List<RfidTagRead>

    fun release()
}

