package ru.heatrk.languageapp.core.navigation.api

sealed interface RoutingAction {
    data class NavigateTo(
        val route: Route,
        val options: RoutingOptions = RoutingOptions()
    ) : RoutingAction

    object NavigateBack : RoutingAction
}
