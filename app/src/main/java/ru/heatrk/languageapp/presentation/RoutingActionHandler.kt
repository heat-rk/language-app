package ru.heatrk.languageapp.presentation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ru.heatrk.languageapp.core.navigation.api.RoutingAction
import ru.heatrk.languageapp.presentation.navigation.composeRoute

class RoutingActionHandler(
    private val navController: NavController,
) {
    fun handle(action: RoutingAction) {
        when (action) {
            RoutingAction.NavigateBack -> {
                navController.popBackStack()
            }
            is RoutingAction.NavigateTo -> {
                navController.navigate(
                    route = composeRoute(action.route),
                    navOptions = NavOptions.Builder()
                        .apply {
                            action.popUpTo?.let { popUpToRoute ->
                                setPopUpTo(
                                    route = composeRoute(popUpToRoute, withArgs = false),
                                    inclusive = true
                                )
                            }
                        }
                        .build()
                )
            }
        }
    }
}
