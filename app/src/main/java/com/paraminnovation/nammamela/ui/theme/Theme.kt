package com.paraminnovation.nammamela.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BrandRed,
    onPrimary = SurfaceWhite,
    primaryContainer = BrandRedSoft,
    onPrimaryContainer = BrandRedDark,
    secondary = BrandGold,
    onSecondary = TextPrimary,
    secondaryContainer = BrandGoldSoft,
    onSecondaryContainer = Color(0xFF6A4900),
    background = NeutralBg,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = NeutralBg,
    onSurfaceVariant = TextSecondary,
    outline = Divider
)

// Dark scheme kept simple — app primarily targets light/Blinkit-style cleanliness.
private val DarkColors = darkColorScheme(
    primary = BrandRed,
    onPrimary = SurfaceWhite,
    secondary = BrandGold,
    onSecondary = TextPrimary,
    background = Color(0xFF121212),
    onBackground = SurfaceWhite,
    surface = Color(0xFF1B1B1B),
    onSurface = SurfaceWhite
)

@Composable
fun NammaMelaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
