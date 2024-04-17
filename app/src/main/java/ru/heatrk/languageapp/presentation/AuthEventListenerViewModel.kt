package ru.heatrk.languageapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.auth.api.domain.AuthEvent
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOption
import ru.heatrk.languageapp.features.splash.api.navigation.SPLASH_SCREEN_ROUTE_PATH

class AuthEventListenerViewModel(
    private val authRepository: AuthRepository,
    private val router: Router,
) : ViewModel() {

    init {
        observeAuthEvents()
    }

    private fun observeAuthEvents() {
        authRepository.authEvents
            .onEach { event ->
                when (event) {
                    AuthEvent.NotAuthenticated ->
                        handleAuthUnauthorizedEvent()
                    AuthEvent.Authenticated ->
                        Unit
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun handleAuthUnauthorizedEvent() {
        val canBeHandled = router.currentRoute != null &&
                    router.currentRoute != SPLASH_SCREEN_ROUTE_PATH &&
                    router.parentRoute?.path != AUTH_GRAPH_ROUTE_PATH

        if (canBeHandled) {
            router.navigate(
                routePath = AUTH_GRAPH_ROUTE_PATH,
                options = listOf(
                    RoutingOption.ClearStack
                )
            )
        }
    }
}
