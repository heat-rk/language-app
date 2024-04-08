package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class AppScaffoldController(
    val snackbarHostState: SnackbarHostState,
) {
    val appBarStates = mutableStateListOf<AppBarState>()
    val systemBarsColors = mutableStateListOf<AppSystemBarsColors>()
    var isNavigationTransitionRunning by mutableStateOf(false)
}

@Composable
fun AppScaffoldControllerEffect(
    appBarState: AppBarState = AppBarState.Hidden,
    appSystemBarsColors: AppSystemBarsColors = AppSystemBarsColors.Default,
) {
    val appScaffoldController = LocalAppScaffoldController.current

    DisposableEffect(
        appScaffoldController,
        appBarState.key,
    ) {
        appScaffoldController.appBarStates.add(appBarState)

        onDispose {
            appScaffoldController.appBarStates
                .removeIf { it.key == appBarState.key }
        }
    }

    DisposableEffect(
        appScaffoldController,
        appSystemBarsColors.key
    ) {
        appScaffoldController.systemBarsColors.add(appSystemBarsColors)

        onDispose {
            appScaffoldController.systemBarsColors
                .removeIf { it.key == appSystemBarsColors.key }
        }
    }
}

val LocalAppScaffoldController = staticCompositionLocalOf {
    AppScaffoldController(SnackbarHostState())
}
