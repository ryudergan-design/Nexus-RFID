package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onNavigationClick: (() -> Unit)? = null,
    navigationIcon: ImageVector = Icons.Outlined.Menu,
    navigationContentDescription: String = "Abrir menu",
    eyebrow: String? = "Nexus RFID",
    navigationIconBackground: Boolean = true,
    showDivider: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = AppColors.TopBarBlue,
        contentColor = AppColors.TopBarOnBlue
    ) {
        Column {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        if (!eyebrow.isNullOrBlank()) {
                            Text(
                                text = eyebrow,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppColors.TopBarOnBlue.copy(alpha = 0.72f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.TopBarOnBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    if (onNavigationClick != null) {
                        val navigationModifier = if (navigationIconBackground) {
                            Modifier
                                .size(40.dp)
                                .background(
                                    color = AppColors.TopBarOnBlue.copy(alpha = 0.10f),
                                    shape = AppShapes.input
                                )
                        } else {
                            Modifier.size(40.dp)
                        }

                        IconButton(
                            onClick = onNavigationClick,
                            modifier = navigationModifier
                        ) {
                            Icon(
                                imageVector = navigationIcon,
                                contentDescription = navigationContentDescription,
                                tint = AppColors.TopBarOnBlue
                            )
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = AppColors.TopBarOnBlue,
                    navigationIconContentColor = AppColors.TopBarOnBlue,
                    actionIconContentColor = AppColors.TopBarOnBlue
                )
            )

            if (showDivider) {
                HorizontalDivider(color = AppColors.TopBarOnBlue.copy(alpha = 0.08f))
            }
        }
    }
}
