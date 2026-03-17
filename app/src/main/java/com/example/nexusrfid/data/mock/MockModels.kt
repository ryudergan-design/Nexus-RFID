package com.example.nexusrfid.data.mock

import com.example.nexusrfid.rfid.RfidDevice

data class DepartmentListItem(
    val code: String,
    val name: String
)

data class InventoryCardItem(
    val title: String,
    val description: String,
    val dateTime: String,
    val responsible: String,
    val systemStock: Int,
    val selected: Boolean = false
)

data class DrawerMenuItem(
    val label: String,
    val route: String,
    val children: List<DrawerMenuItem> = emptyList()
)

enum class ProductTargetState {
    Pending,
    Found,
    Divergent
}

data class ProductListItem(
    val name: String,
    val code: String,
    val reducedCode: String,
    val ean13: String,
    val tagCode: String,
    val targetState: ProductTargetState
)

data class SearchCounterSummary(
    val readCount: Int,
    val foundCount: Int
)

data class SearchTargetItem(
    val key: String,
    val label: String,
    val targetState: ProductTargetState? = null
)

data class SearchTypeOption(
    val key: String,
    val label: String,
    val dialogTitle: String? = null,
    val inputPlaceholder: String? = null,
    val numericInput: Boolean = false
) {
    val requiresManualEntry: Boolean
        get() = dialogTitle != null
}

data class RfidTargetPreviewItem(
    val epc: String,
    val proximityPercent: Int,
    val proximityLabel: String,
    val matchedProductName: String? = null
)

data class RfidSettingsPreviewState(
    val statusMessage: String,
    val connectedDevice: RfidDevice? = null,
    val soundEnabled: Boolean = true,
    val isSearchingDevices: Boolean = false
)
