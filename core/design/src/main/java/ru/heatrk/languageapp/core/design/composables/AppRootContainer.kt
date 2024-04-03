package ru.heatrk.languageapp.core.design.composables

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffold
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.heatrk.languageapp.core.design.composables.scaffold.rememberAppScaffoldController
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun AppRootContainer(
    content: @Composable BoxScope.(isDarkTheme: Boolean) -> Unit,
) {
    val appScaffoldController = rememberAppScaffoldController(
        snackbarHostState = SnackbarHostState(),
    )

    val currentAppBarState = appScaffoldController.appBarStates.lastOrNull()
        ?: AppBarState.Hidden

    AppTheme { isDarkTheme ->
        AppScaffold(
            snackbarHostState = appScaffoldController.snackbarHostState,
            appBarState = currentAppBarState,
        ) {
            CompositionLocalProvider(LocalAppScaffoldController provides appScaffoldController) {
                content(isDarkTheme)
            }
        }
    }
}
