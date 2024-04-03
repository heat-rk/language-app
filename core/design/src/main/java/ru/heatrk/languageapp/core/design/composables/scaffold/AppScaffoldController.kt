package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class AppScaffoldController(
    val snackbarHostState: SnackbarHostState,
) {
    val appBarStates = mutableStateListOf<AppBarState>()
}

@Composable
fun rememberAppScaffoldController(
    snackbarHostState: SnackbarHostState,
) = remember {
    AppScaffoldController(
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun AppScaffoldControllerEffect(
    appBarState: AppBarState = AppBarState.Hidden,
) {
    val appScaffoldController = LocalAppScaffoldController.current

    DisposableEffect(appScaffoldController) {
        appScaffoldController.appBarStates.add(appBarState)

        onDispose {
            appScaffoldController.appBarStates
                .removeIf { it.key == appBarState.key }
        }
    }
}

val LocalAppScaffoldController = staticCompositionLocalOf {
    AppScaffoldController(SnackbarHostState())
}
