package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun AppDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    numericInput: Boolean = false,
    confirmEnabled: Boolean = value.isNotBlank()
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = AppShapes.modal,
            colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
            border = BorderStroke(1.dp, AppColors.Divider),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppSpacing.lg,
                            end = AppSpacing.lg,
                            top = AppSpacing.lg
                        ),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.TextPrimary
                    )
                    Text(
                        text = "Informe o valor para continuar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary
                    )
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .padding(horizontal = AppSpacing.lg),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true,
                    shape = AppShapes.input,
                    placeholder = {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.TextSecondary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (numericInput) KeyboardType.Number else KeyboardType.Text
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = AppColors.FieldBackground,
                        unfocusedContainerColor = AppColors.FieldBackground,
                        disabledContainerColor = AppColors.FieldBackground,
                        errorContainerColor = AppColors.FieldBackground,
                        focusedBorderColor = AppColors.BrandSignalBlue.copy(alpha = 0.44f),
                        unfocusedBorderColor = AppColors.Divider,
                        cursorColor = AppColors.PrimaryActionBlue,
                        focusedTextColor = AppColors.TextPrimary,
                        unfocusedTextColor = AppColors.TextPrimary
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppSpacing.md,
                            end = AppSpacing.md,
                            bottom = AppSpacing.md
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancelar",
                            color = AppColors.TextSecondary
                        )
                    }

                    Button(
                        onClick = onConfirm,
                        enabled = confirmEnabled,
                        shape = AppShapes.button,
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.TopBarBlue,
                            contentColor = AppColors.TopBarOnBlue
                        )
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}
