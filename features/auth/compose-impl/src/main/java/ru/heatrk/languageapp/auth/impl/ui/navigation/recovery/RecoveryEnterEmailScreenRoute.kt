package ru.heatrk.languageapp.auth.impl.ui.navigation.recovery

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.enter_email.RecoveryEnterEmailScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class RecoveryEnterEmailScreenRoute(
    private val recoveryViewModelProvider: () -> RecoveryFlowViewModel,
) : Route.Screen(
    path = RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        RecoveryEnterEmailScreen(viewModel = recoveryViewModelProvider())
    }
}

internal const val RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH = "recovery_enter_email"
