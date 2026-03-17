package com.example.nexusrfid.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppColors {
    val TopBarBlue = Color(0xFF294765)
    val TopBarOnBlue = Color(0xFFF7FBFF)
    val PrimaryActionBlue = Color(0xFF4F86E8)
    val BrandSignalBlue = Color(0xFF6CC6FF)
    val ScreenBackground = Color(0xFFF2F6FB)
    val CardSurface = Color(0xFFFFFFFF)
    val MutedSurface = Color(0xFFE7EEF7)
    val HeroSurface = Color(0xFFEAF1F8)
    val HeroSurfaceStrong = Color(0xFFD7E6F5)
    val AccentSurface = Color(0xFFF1F7FE)
    val AccentBorder = Color(0xFFC6D7EA)
    val Divider = Color(0xFFD7E1EC)
    val TextPrimary = Color(0xFF14233A)
    val TextSecondary = Color(0xFF6B7C90)
    val PositiveGreen = Color(0xFF58B78A)
    val PositiveBorder = Color(0xFFACDCC4)
    val DangerSoft = Color(0xFFF8E0E4)
    val DarkModal = Color(0xFF223046)
    val FieldBackground = Color(0xFFF7FAFD)
    val BrandMarkSurface = Color(0xFF23415F)
    val BrandMarkOnSurface = Color(0xFFFFFFFF)
}

val NexusLightColorScheme = lightColorScheme(
    primary = AppColors.PrimaryActionBlue,
    onPrimary = AppColors.TopBarOnBlue,
    secondary = AppColors.PositiveGreen,
    onSecondary = AppColors.TopBarOnBlue,
    tertiary = AppColors.TextSecondary,
    background = AppColors.ScreenBackground,
    onBackground = AppColors.TextPrimary,
    surface = AppColors.CardSurface,
    onSurface = AppColors.TextPrimary,
    surfaceVariant = AppColors.FieldBackground,
    onSurfaceVariant = AppColors.TextSecondary,
    outline = AppColors.Divider,
    outlineVariant = AppColors.MutedSurface
)
