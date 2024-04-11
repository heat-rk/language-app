package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.activity.SystemBarStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.design.styles.darkAppColors

@Immutable
data class AppSystemBarsStyles(
    val key: String,
    val statusBar: SystemBarStyle,
    val navigationBar: SystemBarStyle,
) {
    companion object {
        @Composable
        fun default(isDarkTheme: Boolean): AppSystemBarsStyles {
            return AppSystemBarsStyles(
                key = "default",
                statusBar = SystemBarStyle.dark(
                    scrim = AppTheme.colors.primary.toArgb()
                ),
                navigationBar = if (isDarkTheme) {
                    SystemBarStyle.dark(scrim = AppTheme.colors.background.toArgb())
                } else {
                    SystemBarStyle.light(
                        scrim = AppTheme.colors.background.toArgb(),
                        darkScrim = darkAppColors.background.toArgb(),
                    )
                },
            )
        }
    }
}

val LocalAppSystemBarsStylesDefault = staticCompositionLocalOf<AppSystemBarsStyles> {
    throw IllegalStateException("No AppSystemBarsStyles provided")
}
