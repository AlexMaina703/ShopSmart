package com.shopsmart.app.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ------------------------------------------------------------
//  LIGHT SCHEME
// ------------------------------------------------------------
private val LightColorScheme = lightColorScheme(
    primary          = Primary,
    onPrimary        = OnPrimaryLight,
    primaryContainer = Primary,
    onPrimaryContainer = OnPrimaryLight,

    secondary        = Secondary,
    onSecondary      = TextPrimary,

    tertiary         = PrimaryDark,
    onTertiary       = OnPrimaryLight,

    background       = Background,
    onBackground     = TextPrimary,

    surface          = Surface,
    onSurface        = TextPrimary,

    surfaceVariant   = DividerColor,
    onSurfaceVariant = TextSecondary,

    outline          = DividerColor,

    error            = Error,
    onError          = OnPrimaryLight,
)

// ------------------------------------------------------------
//  DARK SCHEME
// ------------------------------------------------------------
private val DarkColorScheme = darkColorScheme(
    primary          = PrimaryDarkTheme,
    onPrimary        = OnPrimaryDark,
    primaryContainer = PrimaryDarkDark,
    onPrimaryContainer = OnPrimaryLight,

    secondary        = SecondaryDarkTheme,
    onSecondary      = TextPrimaryDark,

    tertiary         = PrimaryDarkDark,
    onTertiary       = OnPrimaryLight,

    background       = BackgroundDark,
    onBackground     = TextPrimaryDark,

    surface          = SurfaceDark,
    onSurface        = TextPrimaryDark,

    surfaceVariant   = DividerDark,
    onSurfaceVariant = TextSecondaryDark,

    outline          = DividerDark,

    error            = ErrorDark,
    onError          = OnPrimaryLight,
)

@Composable
fun ShopSmartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // false → keep ShopEasy brand orange. true → wallpaper (Android 12+).
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            // If the status bar is dark → light icons; if it's light → dark icons.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content,
    )
}