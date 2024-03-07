package ru.heatrk.languageapp.core.navigation.compose_impl

import ru.heatrk.languageapp.core.navigation.api.RoutingOptions

internal sealed interface RoutingAction {
    data class NavigateTo(
        val routePath: String,
        val options: RoutingOptions = RoutingOptions()
    ) : RoutingAction

    data object NavigateBack : RoutingAction
}
