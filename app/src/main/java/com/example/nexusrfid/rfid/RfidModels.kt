package com.example.nexusrfid.rfid

enum class CollectorModel(val label: String) {
    C72("C72"),
    R6("R6")
}

enum class RfidConnectionState {
    Idle,
    Scanning,
    Connecting,
    Connected,
    Disconnected,
    Error
}

data class RfidDevice(
    val name: String,
    val address: String,
    val rssi: Int? = null,
    val bonded: Boolean = false
) {
    val displayName: String
        get() = name.ifBlank {
            val suffix = address.takeLast(8).ifBlank { "pareado" }
            "Coletor $suffix"
        }
}

data class RfidTagRead(
    val epc: String,
    val rssi: Int? = null,
    val tid: String? = null,
    val seenAtMillis: Long = System.currentTimeMillis()
)
