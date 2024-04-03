package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Stable
class AppScaffoldController(
    val snackbarHostState: SnackbarHostState,
    initialAppBarState: AppBarState = AppBarState.Hidden,
    initialAppBarContainerColor: Color = Color.Transparent,
    initialAppBarContentColor: Color = Color.Transparent,
) {
    var appBarState by mutableStateOf<AppBarState>(initialAppBarState)
    var appBarContainerColor by mutableStateOf(initialAppBarContainerColor)
    var appBarContentColor by mutableStateOf(initialAppBarContentColor)
}

@Composable
fun rememberAppScaffoldController(
    snackbarHostState: SnackbarHostState,
    initialAppBarState: AppBarState = AppBarState.Hidden,
    initialAppBarContainerColor: Color = Color.Transparent,
    initialAppBarContentColor: Color = Color.Transparent,
) = remember {
    AppScaffoldController(
        snackbarHostState = snackbarHostState,
        initialAppBarState = initialAppBarState,
        initialAppBarContainerColor = initialAppBarContainerColor,
        initialAppBarContentColor = initialAppBarContentColor,
    )
}

@Composable
fun AppScaffoldControllerEffect(
    appBarState: AppBarState = AppBarState.Hidden,
    appBarContainerColor: Color = AppTheme.colors.primary,
    appBarContentColor: Color = AppTheme.colors.onPrimary,
) {
    val appScaffoldController = LocalAppScaffoldController.current

    LaunchedEffect(appScaffoldController) {
        appScaffoldController.appBarState = appBarState
        appScaffoldController.appBarContainerColor = appBarContainerColor
        appScaffoldController.appBarContentColor = appBarContentColor
    }
}

val LocalAppScaffoldController = staticCompositionLocalOf {
    AppScaffoldController(SnackbarHostState())
}
