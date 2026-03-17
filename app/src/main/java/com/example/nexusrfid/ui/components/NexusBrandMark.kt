package com.example.nexusrfid.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusrfid.ui.theme.AppColors
import com.example.nexusrfid.ui.theme.AppSpacing
import com.example.nexusrfid.ui.theme.NexusRFIDTheme

@Composable
fun NexusRfidBrandLockup(
    modifier: Modifier = Modifier,
    subtitle: String = "Operacao inteligente de inventario"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
    ) {
        NexusRfidBrandMark()
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        Text(
            text = "Nexus RFID",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = AppColors.TextPrimary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.16.sp),
            color = AppColors.TextSecondary
        )
    }
}

@Composable
fun NexusRfidBrandMark(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(76.dp)) {
        val radius = 22.dp.toPx()
        val stroke = Stroke(
            width = size.width * 0.11f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        drawRoundRect(
            color = AppColors.BrandMarkSurface,
            cornerRadius = CornerRadius(radius, radius)
        )

        val path = Path().apply {
            moveTo(size.width * 0.24f, size.height * 0.76f)
            lineTo(size.width * 0.24f, size.height * 0.26f)
            lineTo(size.width * 0.50f, size.height * 0.58f)
            lineTo(size.width * 0.76f, size.height * 0.26f)
            lineTo(size.width * 0.76f, size.height * 0.76f)
        }

        drawPath(
            path = path,
            color = AppColors.BrandMarkOnSurface,
            style = stroke
        )

        drawArc(
            color = AppColors.BrandSignalBlue.copy(alpha = 0.62f),
            startAngle = -42f,
            sweepAngle = 58f,
            useCenter = false,
            topLeft = Offset(size.width * 0.48f, size.height * 0.05f),
            size = Size(size.width * 0.36f, size.height * 0.36f),
            style = Stroke(width = size.width * 0.04f, cap = StrokeCap.Round)
        )

        drawArc(
            color = AppColors.BrandSignalBlue,
            startAngle = -42f,
            sweepAngle = 58f,
            useCenter = false,
            topLeft = Offset(size.width * 0.57f, size.height * 0.14f),
            size = Size(size.width * 0.22f, size.height * 0.22f),
            style = Stroke(width = size.width * 0.05f, cap = StrokeCap.Round)
        )

        drawCircle(
            color = AppColors.BrandSignalBlue,
            radius = size.width * 0.03f,
            center = Offset(size.width * 0.69f, size.height * 0.25f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NexusRfidBrandLockupPreview() {
    NexusRFIDTheme {
        NexusRfidBrandLockup()
    }
}
