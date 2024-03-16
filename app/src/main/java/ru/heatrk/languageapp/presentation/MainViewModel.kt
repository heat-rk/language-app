package ru.heatrk.languageapp.presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.heatrk.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.core.navigation.api.DeepLinkRouter
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOption
import ru.heatrk.languageapp.features.splash.api.navigation.SPLASH_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH

class MainViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val router: Router,
    private val deepLinkRouters: List<DeepLinkRouter>,
    intent: Intent,
) : ViewModel() {
    private val _isInitializationFinished = MutableStateFlow(false)
    val isInitializationFinished = _isInitializationFinished.asStateFlow()

    init {
        viewModelScope.launch {
            var nextScreenRoutePath: String? = null

            val delayJob = launch {
                delay(INITIALIZATION_DELAY_MILLIS)
            }

            val splashInitializationJob = launch {
                nextScreenRoutePath = if (onboardingRepository.getUnwatchedUnits().isEmpty()) {
                    AUTH_GRAPH_ROUTE_PATH
                } else {
                    ONBOARDING_SCREEN_ROUTE_PATH
                }
            }

            delayJob.join()
            splashInitializationJob.join()

            nextScreenRoutePath?.let { route ->
                router.navigate(
                    routePath = route,
                    options = listOf(
                        RoutingOption.PopUpTo(
                            routePath = SPLASH_SCREEN_ROUTE_PATH,
                            inclusive = true
                        )
                    )
                )
            }

            handleDeepLinks(intent)

            _isInitializationFinished.value = true
        }
    }

    fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            viewModelScope.launch {
                handleDeepLinks(intent)
            }
        }
    }

    private suspend fun handleDeepLinks(intent: Intent) {
        deepLinkRouters.any { router -> router.handle(intent) }
    }

    companion object {
        private const val INITIALIZATION_DELAY_MILLIS = 2000L
    }
}
