package ru.heatrk.languageapp.core.navigation.compose_impl

import ru.heatrk.languageapp.core.navigation.api.RoutingOption

internal sealed interface RoutingAction {
    data class NavigateTo(
        val routePath: String,
        val options: List<RoutingOption>,
    ) : RoutingAction

    data object NavigateBack : RoutingAction
}
