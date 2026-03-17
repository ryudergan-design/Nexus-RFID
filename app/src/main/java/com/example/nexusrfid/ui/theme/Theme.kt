package com.example.nexusrfid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun NexusRFIDTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NexusLightColorScheme,
        typography = AppTypography.material,
        shapes = NexusShapes,
        content = content
    )
}
