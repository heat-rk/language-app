package ru.heatrk.languageapp.core.design.styles

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isNightMode(),
    content: @Composable (isDarkTheme: Boolean) -> Unit
) {
    val appColors = if (isDarkTheme) {
        darkAppColors
    } else {
        lightAppColors
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = darkAppColors.materialColors,
            shapes = appShapes,
            typography = appTypography,
            content = {
                content(isDarkTheme)
            }
        )
    }
}

@Composable
fun isNightMode() = when (AppCompatDelegate.getDefaultNightMode()) {
    AppCompatDelegate.MODE_NIGHT_NO -> false
    AppCompatDelegate.MODE_NIGHT_YES -> true
    else -> isSystemInDarkTheme()
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography
}
