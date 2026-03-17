package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes

@Composable
fun ActionButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = AppShapes.button,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.PrimaryActionBlue,
            contentColor = AppColors.TopBarOnBlue,
            disabledContainerColor = AppColors.MutedSurface,
            disabledContentColor = AppColors.TextSecondary
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 2.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ActionButtonOutline(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = AppColors.Divider,
    contentColor: Color = AppColors.TextPrimary,
    containerColor: Color = AppColors.FieldBackground,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = AppShapes.button,
        border = BorderStroke(1.dp, borderColor),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = AppColors.FieldBackground,
            disabledContentColor = AppColors.TextSecondary
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 2.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
