package com.example.nexusrfid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

@Composable
fun SearchHeader(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    sectionTitle: String? = null,
    placeholder: String = "Digite nome ou codigo",
    buttonLabel: String = "Buscar"
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.AccentSurface),
        border = BorderStroke(1.dp, AppColors.AccentBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppSpacing.lg,
                        end = AppSpacing.lg,
                        top = AppSpacing.lg
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs)) {
                    Text(
                        text = (sectionTitle ?: "Consulta de produtos").uppercase(),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 11.sp
                        ),
                        color = AppColors.TextSecondary
                    )
                    Text(
                        text = "Pesquisar produto",
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.TextPrimary
                    )
                }

                Badge(
                    containerColor = AppColors.TopBarBlue.copy(alpha = 0.10f),
                    contentColor = AppColors.TopBarBlue
                ) {
                    Text(
                        text = "CATALOGO",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp)
                    )
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppSpacing.lg,
                        end = AppSpacing.lg,
                        top = if (sectionTitle.isNullOrBlank()) AppSpacing.lg else 0.dp,
                        bottom = AppSpacing.lg
                    )
            ) {
                val buttonWidth = 116.dp
                val fieldWidth = maxWidth - buttonWidth - AppSpacing.sm

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .width(fieldWidth)
                            .height(52.dp),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppColors.TextSecondary
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = AppColors.TextSecondary
                            )
                        },
                        shape = AppShapes.input,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = AppColors.FieldBackground,
                            unfocusedContainerColor = AppColors.FieldBackground,
                            disabledContainerColor = AppColors.FieldBackground,
                            errorContainerColor = AppColors.FieldBackground,
                            focusedBorderColor = AppColors.BrandSignalBlue.copy(alpha = 0.44f),
                            unfocusedBorderColor = AppColors.Divider,
                            cursorColor = AppColors.PrimaryActionBlue,
                            focusedLeadingIconColor = AppColors.TextSecondary,
                            unfocusedLeadingIconColor = AppColors.TextSecondary,
                            focusedTextColor = AppColors.TextPrimary,
                            unfocusedTextColor = AppColors.TextPrimary
                        )
                    )

                    Button(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .width(buttonWidth)
                            .height(52.dp),
                        shape = AppShapes.button,
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.PrimaryActionBlue,
                            contentColor = AppColors.TopBarOnBlue
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null
                            )
                            Text(
                                text = buttonLabel,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 12.sp,
                                    letterSpacing = 0.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
