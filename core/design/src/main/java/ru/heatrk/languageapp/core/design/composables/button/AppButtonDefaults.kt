package ru.heatrk.languageapp.core.design.composables.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.heatrk.languageapp.core.design.styles.AppTheme

internal object AppButtonDefaults {
    @Composable
    fun colors(
        idleColor: Color = AppTheme.colors.secondary,
        idleContentColor: Color = AppTheme.colors.onSecondary,
        loadingColor: Color = AppTheme.colors.progressBackground,
        loadingContentColor: Color = AppTheme.colors.primaryContainer,
        successColor: Color = AppTheme.colors.successColor,
        successContentColor: Color = AppTheme.colors.onSuccessColor,
        errorColor: Color = AppTheme.colors.error,
        errorContentColor: Color = AppTheme.colors.onError,
    ) = AppButtonColors(
        idleColor = idleColor,
        idleContentColor = idleContentColor,
        loadingColor = loadingColor,
        loadingContentColor = loadingContentColor,
        successColor = successColor,
        successContentColor = successContentColor,
        errorColor = errorColor,
        errorContentColor = errorContentColor,
    )
}
