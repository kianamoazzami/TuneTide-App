package com.example.tunetide.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = PurpleBackground,
    primaryVariant = PurpleAccent,
    secondary = PurpleDark,
    background = PurpleBackground,
    surface = Greyish,
    onPrimary = PurpleBackground,
    onSecondary = PurpleDark,
    onBackground = PurpleBackground,
    onSurface = Black,
)

// TODO: Add dark mode
private val DarkColorPalette = LightColorPalette

@Composable
fun TuneTideTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}