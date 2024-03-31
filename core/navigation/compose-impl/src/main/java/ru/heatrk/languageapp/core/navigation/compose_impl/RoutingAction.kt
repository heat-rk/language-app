package ru.heatrk.languageapp.core.navigation.compose_impl

import ru.heatrk.languageapp.core.navigation.api.RoutingOption

internal sealed interface RoutingAction {
    data class NavigateTo(
        val routePath: String,
        val options: List<RoutingOption>,
        val arguments: Map<String, Any>,
    ) : RoutingAction

    data object NavigateBack : RoutingAction
}
