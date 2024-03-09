package ru.heatrk.languageapp.core.navigation.compose_impl

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import ru.heatrk.languageapp.common.utils.currentRoute
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOptions

class ComposeRouter : Router {

    private val _actions = Channel<RoutingAction>(Channel.BUFFERED)
    private val actions = _actions.receiveAsFlow()

    private var navController: NavController? = null

    override val isFirstDestination
        get() = navController?.previousBackStackEntry == null

    override val currentRoute: String?
        get() = navController?.currentRoute

    override suspend fun navigate(routePath: String, options: RoutingOptions) {
        action(RoutingAction.NavigateTo(routePath, options))
    }

    override suspend fun navigateBack() {
        action(RoutingAction.NavigateBack)
    }

    fun attachNavController(
        navController: NavController,
        coroutineScope: CoroutineScope,
    ) {
        this.navController = navController

        actions
            .onEach { action -> handleRoutingAction(navController, action) }
            .flowOn(Dispatchers.Main)
            .launchIn(coroutineScope)
    }

    fun detachNavController() {
        this.navController = null
    }

    private fun handleRoutingAction(
        navController: NavController,
        action: RoutingAction
    ) {
        when (action) {
            is RoutingAction.NavigateTo ->
                handleNavigateToRouting(navController, action.routePath, action.options)
            RoutingAction.NavigateBack ->
                handleNavigateBackRouting(navController)
        }
    }

    private fun handleNavigateToRouting(
        navController: NavController,
        routePath: String,
        options: RoutingOptions
    ) {
        navController.navigate(routePath) {
            if (options.shouldBePopUp) {
                navController.currentRoute?.let { currentRoute ->
                    popUpTo(currentRoute) {
                        inclusive = true
                    }
                }
            }

            if (options.singleTop) {
                launchSingleTop = true

                popUpTo(routePath) {
                    inclusive = false
                }
            }
        }
    }

    private fun handleNavigateBackRouting(
        navController: NavController,
    ) {
        navController.popBackStack()
    }

    private suspend fun action(action: RoutingAction) {
        _actions.send(action)
    }
}
