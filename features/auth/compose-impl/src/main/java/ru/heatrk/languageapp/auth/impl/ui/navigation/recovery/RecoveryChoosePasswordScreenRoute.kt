package ru.heatrk.languageapp.auth.impl.ui.navigation.recovery

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.choose_password.RecoveryChoosePasswordScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class RecoveryChoosePasswordScreenRoute(
    private val recoveryViewModelProvider: () -> RecoveryFlowViewModel,
) : Route.Screen(
    path = RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        RecoveryChoosePasswordScreen(viewModel = recoveryViewModelProvider())
    }
}

internal const val RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH = "recovery_choose_password"
