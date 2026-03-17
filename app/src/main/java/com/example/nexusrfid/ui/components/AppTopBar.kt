package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.foundation.border
import androidx.compose.ui.unit.sp
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
    eyebrow: String? = "NEXUS RFID",
    navigationIconBackground: Boolean = true,
    showDivider: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = Color.Transparent,
        contentColor = AppColors.TopBarOnBlue
    ) {
        Column {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.padding(start = if (onNavigationClick != null) 12.dp else 0.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        if (!eyebrow.isNullOrBlank()) {
                            Text(
                                text = eyebrow.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 10.sp,
                                color = AppColors.BrandSignalBlue,
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
                        IconButton(
                            onClick = onNavigationClick,
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = AppColors.CardSurfaceHighlight,
                                    shape = AppShapes.input
                                )
                                .border(0.5.dp, AppColors.Divider, AppShapes.input)
                        ) {
                            Icon(
                                imageVector = navigationIcon,
                                contentDescription = navigationContentDescription,
                                tint = AppColors.BrandSignalBlue
                            )
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = AppColors.TopBarOnBlue,
                    navigationIconContentColor = AppColors.BrandSignalBlue,
                    actionIconContentColor = AppColors.TopBarOnBlue
                )
            )

            if (showDivider) {
                HorizontalDivider(color = AppColors.Divider, thickness = 0.5.dp)
            }
        }
    }
}
