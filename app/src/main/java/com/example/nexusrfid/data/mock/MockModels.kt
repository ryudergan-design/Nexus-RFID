package com.example.nexusrfid.data.mock

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
    val route: String
)

data class ProductListItem(
    val name: String,
    val code: String
)
