package com.realestateagency.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF2E7D32),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFC8E6C9),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF1B5E20),
    secondary = androidx.compose.ui.graphics.Color(0xFF1976D2),
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFBBDEFB),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF0D47A1),
    error = androidx.compose.ui.graphics.Color(0xFFB3261E),
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = androidx.compose.ui.graphics.Color(0xFFF9DEDC),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410E0B),
    background = androidx.compose.ui.graphics.Color(0xFFFAFBF8),
    onBackground = androidx.compose.ui.graphics.Color(0xFF1A1C1A),
    surface = androidx.compose.ui.graphics.Color(0xFFFAFBF8),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1A1C1A),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFE0E4DE),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF46483F)
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFFA5D6A7),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF0D3818),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF1B5E20),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFC8E6C9),
    secondary = androidx.compose.ui.graphics.Color(0xFF90CAF9),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF0D47A1),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF1565C0),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFBBDEFB),
    error = androidx.compose.ui.graphics.Color(0xFFF2B8B5),
    onError = androidx.compose.ui.graphics.Color(0xFF601410),
    errorContainer = androidx.compose.ui.graphics.Color(0xFF8C1D18),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFFF9DEDC),
    background = androidx.compose.ui.graphics.Color(0xFF1A1C1A),
    onBackground = androidx.compose.ui.graphics.Color(0xFFE2E3DE),
    surface = androidx.compose.ui.graphics.Color(0xFF1A1C1A),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE2E3DE),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF46483F),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFC9CCC3)
)

@Composable
fun RealEstateAgencyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
