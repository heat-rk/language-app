package ru.heatrk.languageapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.heatrk.languageapp.auth.api.ui.navigation.SIGN_IN_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOptions
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH

class MainViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val router: Router,
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
                    SIGN_IN_SCREEN_ROUTE_PATH
                } else {
                    ONBOARDING_SCREEN_ROUTE_PATH
                }
            }

            delayJob.join()
            splashInitializationJob.join()

            nextScreenRoutePath?.let { route ->
                router.navigate(
                    routePath = route,
                    options = RoutingOptions(
                        shouldBePopUp = true
                    )
                )
            }

            _isInitializationFinished.value = true
        }
    }

    companion object {
        private const val INITIALIZATION_DELAY_MILLIS = 2000L
    }
}
