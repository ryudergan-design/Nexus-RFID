package com.example.nexusrfid.ui.screens.departments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nexusrfid.data.mock.DepartmentListItem
import com.example.nexusrfid.data.mock.MockDataSource
import com.example.nexusrfid.ui.components.AppTopBar
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppShapes
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

@Composable
fun DepartmentsScreen(
    departments: List<DepartmentListItem>,
    onMenuClick: () -> Unit,
    onDepartmentClick: (DepartmentListItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppColors.ScreenBackground,
        topBar = {
            AppTopBar(
                title = "Departamentos",
                eyebrow = null,
                onNavigationClick = onMenuClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.ScreenBackground)
                .padding(innerPadding),
            contentPadding = PaddingValues(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            items(departments) { department ->
                DepartmentEntryCard(
                    department = department,
                    onClick = { onDepartmentClick(department) }
                )
            }
        }
    }
}

@Composable
private fun DepartmentEntryCard(
    department: DepartmentListItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(containerColor = AppColors.FieldBackground),
        border = BorderStroke(1.dp, AppColors.Divider),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg)
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.padding(end = 52.dp),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                Text(
                    text = department.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = department.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(36.dp)
                    .background(AppColors.CardSurface, AppShapes.input)
                    .border(1.dp, AppColors.Divider, AppShapes.input),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = AppColors.TextSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun DepartmentsScreenPreview() {
    NexusRFIDTheme {
        DepartmentsScreen(
            departments = MockDataSource.departments,
            onMenuClick = {},
            onDepartmentClick = {}
        )
    }
}
