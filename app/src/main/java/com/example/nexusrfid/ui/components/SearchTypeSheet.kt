package com.example.nexusrfid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.SearchTypeOption
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.border
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun SearchTypeSheet(
    options: List<SearchTypeOption>,
    onSelect: (SearchTypeOption) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = AppShapes.modal,
            colors = CardDefaults.cardColors(containerColor = AppColors.DarkModal),
            border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.BrandSignalBlue.copy(alpha = 0.44f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                Text(
                    text = "SELECIONE O ALVO",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppSpacing.lg),
                    style = MaterialTheme.typography.labelLarge,
                    color = AppColors.BrandSignalBlue,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.2.sp
                )
                
                HorizontalDivider(
                    color = AppColors.Divider, 
                    modifier = Modifier.padding(vertical = AppSpacing.sm, horizontal = AppSpacing.lg)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                ) {
                    options.forEach { option ->
                        SearchTypeRow(
                            option = option,
                            onClick = { onSelect(option) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTypeRow(
    option: SearchTypeOption,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AppColors.CardSurfaceHighlight, shape = AppShapes.button)
            .border(0.5.dp, AppColors.Divider, AppShapes.button)
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md)
    ) {
        Text(
            text = option.label.uppercase(),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
            color = AppColors.TopBarOnBlue,
            textAlign = TextAlign.Center
        )
    }
}
