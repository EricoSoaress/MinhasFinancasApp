// Local: app/src/main/java/com/erico/minhasfinancasapp/ui/theme/Theme.kt

package com.erico.minhasfinancasapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.erico.minhasfinancasapp.ui.theme.CreamWhite
import com.erico.minhasfinancasapp.ui.theme.DarkSurface

private val DarkColorScheme = darkColorScheme(
    primary = GreenDarkPrimary,
    secondary = GreenDarkSecondary,
    tertiary = GreenDarkSecondary,
    background = Color(0xFF121212),
    surface = DarkSurface,
    primaryContainer = GreenDarkContainer,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onPrimaryContainer = GreenDarkPrimary,
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    tertiary = GreenSecondary,
    background = Color(0xFFF7FDF8),
    surface = CreamWhite,
    primaryContainer = GreenContainer,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF1a1a1a),
    onSurface = Color(0xFF1a1a1a),
    onPrimaryContainer = GreenPrimary,
)

@Composable
fun MinhasFinancasAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}