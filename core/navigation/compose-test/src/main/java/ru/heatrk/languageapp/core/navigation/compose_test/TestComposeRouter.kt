package ru.heatrk.languageapp.core.navigation.compose_test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingAction
import ru.heatrk.languageapp.core.navigation.api.RoutingOptions

class TestComposeRouter(
    private val onNavigate: (route: Route, options: RoutingOptions) -> Unit,
    private val onNavigateBack: () -> Unit
) : Router {
    override val actions: Flow<RoutingAction> = emptyFlow()

    override suspend fun navigate(route: Route, options: RoutingOptions) {
        withContext(Dispatchers.Main) {
            onNavigate(route, options)
        }
    }

    override suspend fun navigateBack() {
        withContext(Dispatchers.Main) {
            onNavigateBack()
        }
    }
}
