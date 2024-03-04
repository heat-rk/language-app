package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class AppScaffoldController(
    val snackbarHostState: SnackbarHostState
) {
    var appBarState by mutableStateOf<AppBarState>(AppBarState.Hidden)
}

@Composable
fun rememberAppScaffoldController(
    snackbarHostState: SnackbarHostState
) = remember {
    AppScaffoldController(
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun AppScaffoldControllerEffect(
    appBarState: AppBarState = AppBarState.Hidden,
) {
    val appScaffoldController = LocalAppScaffoldController.current

    LaunchedEffect(appScaffoldController) {
        appScaffoldController.appBarState = appBarState
    }
}

val LocalAppScaffoldController = staticCompositionLocalOf {
    AppScaffoldController(SnackbarHostState())
}
