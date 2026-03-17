package com.example.nexusrfid.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.components.ActionButtonPrimary
import com.example.nexusrfid.ui.components.NexusRfidBrandLockup
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

private const val DemoUsername = "jeferson"
private const val DemoPassword = "Fal.990544"

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf(DemoUsername) }
    var password by remember { mutableStateOf(DemoPassword) }
    var submitAttempted by remember { mutableStateOf(false) }

    val usernameError = submitAttempted && username.isBlank()
    val passwordError = submitAttempted && password.isBlank()
    val credentialsError = submitAttempted &&
        username.isNotBlank() &&
        password.isNotBlank() &&
        (username != DemoUsername || password != DemoPassword)

    fun submit() {
        submitAttempted = true
        if (
            username.isNotBlank() &&
            password.isNotBlank() &&
            username == DemoUsername &&
            password == DemoPassword
        ) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.ScreenBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .align(Alignment.TopCenter)
                .background(
                    color = AppColors.TopBarBlue.copy(alpha = 0.06f),
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 28.dp, vertical = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Plataforma Nexus",
                modifier = Modifier
                    .background(AppColors.CardSurface, AppShapes.button)
                    .border(1.dp, AppColors.Divider, AppShapes.button)
                    .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xxs),
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TopBarBlue
            )

            Spacer(modifier = Modifier.height(AppSpacing.lg))

            NexusRfidBrandLockup(
                subtitle = "Operacao segura para leitura e inventario"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 320.dp),
                border = BorderStroke(1.dp, AppColors.AccentBorder),
                colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = AppShapes.modal
            ) {
                Column(
                    modifier = Modifier.padding(AppSpacing.xl),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    Text(
                        text = "Acesso seguro",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.BrandSignalBlue
                    )
                    Text(
                        text = "Entrar no sistema",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppColors.TextPrimary
                    )
                    Text(
                        text = "Use suas credenciais para acessar a operacao RFID com a interface Nexus.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary
                    )
                    Text(
                        text = "Credencial deste build: $DemoUsername",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TopBarBlue
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.xs))

                    LoginField(
                        value = username,
                        onValueChange = {
                            username = it
                            if (submitAttempted) submitAttempted = false
                        },
                        placeholder = "Usuario",
                        icon = Icons.Outlined.Person,
                        isError = usernameError,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                    LoginField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (submitAttempted) submitAttempted = false
                        },
                        placeholder = "Senha",
                        icon = Icons.Outlined.Lock,
                        isError = passwordError,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        visualTransformation = PasswordVisualTransformation(),
                        onSubmit = ::submit
                    )

                    if (submitAttempted && (usernameError || passwordError || credentialsError)) {
                        Text(
                            text = when {
                                usernameError || passwordError -> "Preencha usuario e senha."
                                else -> "Credenciais invalidas para este build."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.md))

                    ActionButtonPrimary(
                        text = "Entrar",
                        onClick = ::submit,
                        modifier = Modifier.height(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isError: Boolean,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onSubmit: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        isError = isError,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSecondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppColors.TextSecondary
            )
        },
        shape = AppShapes.input,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onSubmit?.invoke() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppColors.FieldBackground,
            unfocusedContainerColor = AppColors.FieldBackground,
            disabledContainerColor = AppColors.FieldBackground,
            errorContainerColor = AppColors.FieldBackground,
            focusedBorderColor = AppColors.BrandSignalBlue.copy(alpha = 0.44f),
            unfocusedBorderColor = AppColors.Divider,
            errorBorderColor = AppColors.PrimaryActionBlue,
            cursorColor = AppColors.PrimaryActionBlue,
            focusedLeadingIconColor = AppColors.TextSecondary,
            unfocusedLeadingIconColor = AppColors.TextSecondary,
            errorLeadingIconColor = AppColors.TextSecondary,
            focusedTextColor = AppColors.TextPrimary,
            unfocusedTextColor = AppColors.TextPrimary,
            errorTextColor = AppColors.TextPrimary
        )
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun LoginScreenPreview() {
    NexusRFIDTheme {
        LoginScreen(onLoginSuccess = {})
    }
}
