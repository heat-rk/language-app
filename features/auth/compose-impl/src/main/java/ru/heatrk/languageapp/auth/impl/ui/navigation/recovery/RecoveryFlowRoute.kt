package ru.heatrk.languageapp.auth.impl.ui.navigation.recovery

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlow
import ru.heatrk.languageapp.core.navigation.api.Route

internal class RecoveryFlowRoute(
    private val recoveryViewModelFactory: ViewModelProvider.Factory
) : Route.Screen(
    path = RECOVERY_FLOW_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        RecoveryFlow(viewModel = viewModel(factory = recoveryViewModelFactory))
    }
}

internal const val RECOVERY_FLOW_ROUTE_PATH = "recovery"
