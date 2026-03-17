package com.example.nexusrfid.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object AppShapes {
    val input = RoundedCornerShape(0.dp)
    val card = RoundedCornerShape(0.dp)
    val button = RoundedCornerShape(0.dp)
    val modal = RoundedCornerShape(0.dp)
}

val NexusShapes = Shapes(
    small = AppShapes.input,
    medium = AppShapes.card,
    large = AppShapes.modal
)
