package ru.heatrk.languageapp.core.design.composables.button

import androidx.compose.ui.graphics.Color

internal data class AppButtonColors(
    val idleColor: Color,
    val idleContentColor: Color,
    val loadingColor: Color,
    val loadingContentColor: Color,
    val successColor: Color,
    val successContentColor: Color,
    val errorColor: Color,
    val errorContentColor: Color,
)

internal fun AppButtonColors.containerColor(buttonState: AppButtonState) = when (buttonState) {
    AppButtonState.Idle -> idleColor
    AppButtonState.Loading -> loadingColor
    AppButtonState.Success -> successColor
    AppButtonState.Error -> errorColor
}

internal fun AppButtonColors.contentColor(buttonState: AppButtonState) = when (buttonState) {
    AppButtonState.Idle -> idleContentColor
    AppButtonState.Loading -> loadingContentColor
    AppButtonState.Success -> successContentColor
    AppButtonState.Error -> errorContentColor
}
