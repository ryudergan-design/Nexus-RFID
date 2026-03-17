package com.example.nexusrfid.rfid

enum class CollectorModel(val label: String) {
    C72("C72"),
    R6("R6"),
    MC339U("MC339U")
}

enum class RfidConnectionState {
    Idle,
    Scanning,
    Connecting,
    Connected,
    Disconnected,
    Error
}

enum class ReaderRecognitionState {
    Unknown,
    Checking,
    Recognized,
    NotRecognized
}

data class RfidDevice(
    val name: String,
    val address: String,
    val rssi: Int? = null,
    val bonded: Boolean = false
) {
    val displayName: String
        get() {
            val upperName = name.uppercase()
            return when {
                upperName.contains("R6") -> "LEITOR R6"
                upperName.contains("C72") -> "COLETOR C72"
                upperName.contains("NEXUS") -> "LEITOR NEXUS"
                name.isNotBlank() -> name.uppercase()
                else -> {
                    val suffix = address.replace(":", "").takeLast(4)
                    "COLETOR $suffix"
                }
            }
        }
}

data class RfidTagRead(
    val epc: String,
    val rssi: Int? = null,
    val tid: String? = null,
    val seenAtMillis: Long = System.currentTimeMillis()
)

data class ReaderRecognitionFeedback(
    val recognized: Boolean,
    val deviceName: String? = null
)
