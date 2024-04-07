package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Immutable
data class AppSystemBarsColors(
    val key: String,
    val statusBar: Color,
    val navigationBar: Color,
) {
    companion object {
        val Default
            @Composable
            get() = AppSystemBarsColors(
                key = "default",
                statusBar = AppTheme.colors.primary,
                navigationBar = AppTheme.colors.background,
            )
    }
}
