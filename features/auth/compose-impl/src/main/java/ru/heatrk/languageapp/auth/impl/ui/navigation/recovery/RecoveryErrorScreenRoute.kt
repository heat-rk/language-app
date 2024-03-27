package ru.heatrk.languageapp.auth.impl.ui.navigation.recovery

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.error.RecoveryErrorScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class RecoveryErrorScreenRoute(
    private val recoveryViewModelProvider: () -> RecoveryFlowViewModel,
) : Route.Screen(
    path = RECOVERY_ERROR_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        RecoveryErrorScreen(viewModel = recoveryViewModelProvider())
    }
}

internal const val RECOVERY_ERROR_SCREEN_ROUTE_PATH = "recovery_error"