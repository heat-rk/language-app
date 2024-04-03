package ru.heatrk.languageapp.auth.impl.ui.navigation.recovery

import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.heatrk.languageapp.core.navigation.compose_impl.Route
import ru.heatrk.languageapp.core.navigation.compose_impl.route

internal class RecoveryGraphRoute(
    private val recoveryViewModelProvider: () -> RecoveryFlowViewModel,
) : Route.Graph(
    path = RECOVERY_GRAPH_ROUTE_PATH,
    startDestination = RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH,
    builder = {
        route(RecoveryEnterEmailScreenRoute(recoveryViewModelProvider))
        route(RecoveryCheckEmailScreenRoute(recoveryViewModelProvider))
        route(RecoveryChoosePasswordScreenRoute(recoveryViewModelProvider))
        route(RecoverySuccessScreenRoute(recoveryViewModelProvider))
        route(RecoveryErrorScreenRoute(recoveryViewModelProvider))
    }
)

internal const val RECOVERY_GRAPH_ROUTE_PATH = "recovery_graph"
