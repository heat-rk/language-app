package ru.heatrk.languageapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.auth.api.ui.navigation.LoginScreenRoute
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.api.ui.navigation.OnboardingScreenRoute

class MainViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val router: Router,
) : ViewModel() {
    private val _isInitializationFinished = MutableStateFlow(false)
    val isInitializationFinished = _isInitializationFinished.asStateFlow()

    init {
        viewModelScope.launch {
            var nextScreenRoute: Route? = null

            val delayJob = launch {
                delay(INITIALIZATION_DELAY_MILLIS)
            }

            val splashInitializationJob = launch {
                nextScreenRoute = if (onboardingRepository.getUnwatchedUnits().isEmpty()) {
                    LoginScreenRoute
                } else {
                    OnboardingScreenRoute
                }
            }

            delayJob.join()
            splashInitializationJob.join()

            nextScreenRoute?.let { route ->
                router.navigate(route)
            }

            _isInitializationFinished.value = true
        }
    }

    companion object {
        private const val INITIALIZATION_DELAY_MILLIS = 2000L
    }
}
