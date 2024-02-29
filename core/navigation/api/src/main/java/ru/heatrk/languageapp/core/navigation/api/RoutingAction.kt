package ru.heatrk.languageapp.core.navigation.api

sealed interface RoutingAction {
    data class NavigateTo(val route: Route, val popUpTo: Route? = null) : RoutingAction

    object NavigateBack : RoutingAction
}
