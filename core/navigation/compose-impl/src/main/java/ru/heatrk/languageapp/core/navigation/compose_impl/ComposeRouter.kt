package ru.heatrk.languageapp.core.navigation.compose_impl

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingAction

class ComposeRouter : Router {
    private val _actions = Channel<RoutingAction>(Channel.BUFFERED)
    override val actions = _actions.receiveAsFlow()

    override suspend fun navigate(route: Route) {
        action(RoutingAction.NavigateTo(route))
    }

    override suspend fun navigateBack() {
        action(RoutingAction.NavigateBack)
    }

    private suspend fun action(action: RoutingAction) {
        _actions.send(action)
    }
}