package com.example.nexusrfid.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object AppShapes {
    val input = RoundedCornerShape(12.dp)
    val card = RoundedCornerShape(24.dp)
    val button = RoundedCornerShape(16.dp)
    val modal = RoundedCornerShape(32.dp)
}

val NexusShapes = Shapes(
    small = AppShapes.input,
    medium = AppShapes.card,
    large = AppShapes.modal
)
