package com.example.nexusrfid.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppColors {
    // Canvas e Vidros
    val ScreenBackground = Color(0xFF030712)
    val CardSurface = Color(0x0AFFFFFF) // rgba(255,255,255,0.04)
    val CardSurfaceHighlight = Color(0x14FFFFFF) // rgba(255,255,255,0.08)
    val Divider = Color(0x1AFFFFFF) // rgba(255,255,255,0.10)
    val AccentBorder = Color(0x1AFFFFFF)
    val FieldBackground = Color(0x0AFFFFFF)
    
    // Core System - Fluxo Primário
    val PrimaryActionBlue = Color(0xFF3B82F6) // blue-500
    val BrandSignalBlue = Color(0xFF22D3EE) // cyan-400 (Destaque Neon)
    val TextPrimary = Color(0xFF67E8F9) // cyan-300
    val TextSecondary = Color(0xFFA5F3FC).copy(alpha = 0.6f) // cyan-200 com alpha
    val TopBarBlue = Color(0xFF030712)
    val TopBarOnBlue = Color(0xFFFFFFFF)

    // Espectros Semânticos
    val WarningAmber = Color(0xFFF59E0B) // amber-500
    val AutomationFuchsia = Color(0xFFA855F7) // purple-500
    val PositiveGreen = Color(0xFF10B981) // emerald-500
    val DangerRed = Color(0xFFEF4444) // red-500
    
    // Apoio
    val MutedSurface = Color(0x05FFFFFF)
    val DarkModal = Color(0xFF030712)
    val BrandMarkSurface = Color(0xFF030712)
    val BrandMarkOnSurface = Color(0xFFFFFFFF)

    // Compatibilidade (mapeadas para o tema Dark)
    val AccentSurface = Color(0x0AFFFFFF)
    val HeroSurface = Color(0x0AFFFFFF)
    val HeroSurfaceStrong = Color(0x14FFFFFF)
    val PositiveBorder = Color(0x3310B981)
    val DangerSoft = Color(0x33EF4444)
}

val NexusLightColorScheme = lightColorScheme(
    primary = AppColors.PrimaryActionBlue,
    onPrimary = AppColors.TopBarOnBlue,
    secondary = AppColors.PositiveGreen,
    onSecondary = AppColors.TopBarOnBlue,
    tertiary = AppColors.BrandSignalBlue,
    background = AppColors.ScreenBackground,
    onBackground = AppColors.TopBarOnBlue,
    surface = AppColors.CardSurface,
    onSurface = AppColors.TopBarOnBlue,
    surfaceVariant = AppColors.FieldBackground,
    onSurfaceVariant = AppColors.TextSecondary,
    outline = AppColors.Divider,
    outlineVariant = AppColors.MutedSurface,
    error = AppColors.DangerRed
)
