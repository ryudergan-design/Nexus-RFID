package com.example.nexusrfid.ui.screens.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.ui.navigation.AppDestination
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhasePlaceholderScreen(
    title: String,
    description: String,
    sampleItems: List<String>,
    modifier: Modifier = Modifier,
    destinationItems: List<AppDestination> = emptyList(),
    onDestinationClick: ((AppDestination) -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        TextButton(onClick = onBack) {
                            Text("Voltar")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.TopBarBlue,
                    titleContentColor = AppColors.TopBarOnBlue,
                    navigationIconContentColor = AppColors.TopBarOnBlue
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                    ) {
                        Text(
                            text = "Base em preparacao",
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.TextSecondary
                        )
                    }
                }
            }

            if (sampleItems.isNotEmpty()) {
                item {
                    Text(
                        text = "Referencias rapidas",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppColors.TextPrimary
                    )
                }

                items(sampleItems) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.TextPrimary
                        )
                    }
                }
            }

            if (destinationItems.isNotEmpty() && onDestinationClick != null) {
                item {
                    Text(
                        text = "Fluxos mapeados",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppColors.TextPrimary
                    )
                }

                items(destinationItems) { destination ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppColors.CardSurface)
                            .clickable { onDestinationClick(destination) }
                    ) {
                        Text(
                            text = destination.title,
                            modifier = Modifier.padding(
                                horizontal = AppSpacing.sm,
                                vertical = AppSpacing.sm
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            text = destination.summary,
                            modifier = Modifier.padding(
                                start = AppSpacing.sm,
                                end = AppSpacing.sm,
                                bottom = AppSpacing.sm
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.TextSecondary
                        )
                        HorizontalDivider(color = AppColors.Divider)
                    }
                }
            }
        }
    }
}
