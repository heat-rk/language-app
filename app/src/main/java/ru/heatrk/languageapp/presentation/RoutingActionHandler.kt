package ru.heatrk.languageapp.presentation

import androidx.navigation.NavController
import ru.heatrk.languageapp.core.navigation.api.RoutingAction
import ru.heatrk.languageapp.presentation.navigation.composeRoute

class RoutingActionHandler(
    private val navController: NavController,
) {
    private val NavController.currentRoute: String?
        get() = currentBackStackEntry?.destination?.route

    fun handle(action: RoutingAction) {
        when (action) {
            RoutingAction.NavigateBack -> {
                navController.popBackStack()
            }
            is RoutingAction.NavigateTo -> {
                navController.navigate(composeRoute(action.route)) {
                    if (action.options.shouldBePopUp) {
                        navController.currentRoute?.let { currentRoute ->
                            popUpTo(currentRoute) {
                                inclusive = true
                            }
                        }
                    }

                    if (action.options.singleTop) {
                        launchSingleTop = true

                        popUpTo(composeRoute(action.route)) {
                            inclusive = false
                        }
                    }
                }
            }
        }
    }
}
