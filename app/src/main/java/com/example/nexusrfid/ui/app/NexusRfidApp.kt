package com.example.nexusrfid.ui.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nexusrfid.ui.navigation.AppNavHost
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

@Composable
fun NexusRfidApp() {
    NexusRFIDTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppNavHost()
        }
    }
}
