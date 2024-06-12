package com.example.tunetide.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// These will be updated when we add elements to UI.
private val DarkColorPalette = darkColors(
    primary = PurpleBackground,
    primaryVariant = PurpleAccent,
    secondary = PurpleDark
)

private val LightColorPalette = lightColors(
    primary = PurpleBackground,
    primaryVariant = PurpleAccent,
    secondary = PurpleDark

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

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