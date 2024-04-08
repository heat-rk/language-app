package ru.heatrk.languageapp.presentation

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.core.navigation.api.DeepLinkRouter
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOption
import ru.heatrk.languageapp.features.splash.api.navigation.SPLASH_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.main.api.ui.navigation.MAIN_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH

class InitializationViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository,
    private val router: Router,
    private val deepLinkRouters: List<DeepLinkRouter>,
    private val savedStateHandle: SavedStateHandle,
    intent: Intent,
) : ViewModel() {
    private val _isInitializationFinished = MutableStateFlow(
        savedStateHandle.get<Boolean>(HANDLE_INITIALIZATION_FINISHED_KEY) == true
    )

    val isInitializationFinished = _isInitializationFinished.asStateFlow()

    init {
        init(intent)

        isInitializationFinished
            .onEach { isFinished ->
                savedStateHandle[HANDLE_INITIALIZATION_FINISHED_KEY] = isFinished
            }
            .launchIn(viewModelScope)
    }

    fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            viewModelScope.launch {
                handleDeepLinks(intent)
            }
        }
    }

    private fun init(intent: Intent) {
        if (isInitializationFinished.value) {
            return
        }

        viewModelScope.launch {
            val delayJob = launch {
                delay(INITIALIZATION_DELAY_MILLIS)
            }

            val authInitializationJob = launch {
                authRepository.awaitInitialization()
            }

            val splashInitializationJob = async {
                when {
                    onboardingRepository.getUnwatchedUnits().isNotEmpty() ->
                        ONBOARDING_SCREEN_ROUTE_PATH
                    authRepository.hasSavedSession() ->
                        MAIN_GRAPH_ROUTE_PATH
                    else ->
                        AUTH_GRAPH_ROUTE_PATH
                }
            }

            joinAll(
                delayJob,
                authInitializationJob,
                splashInitializationJob
            )

            val nextScreenRoutePath = splashInitializationJob.await()

            router.navigate(
                routePath = nextScreenRoutePath,
                options = listOf(
                    RoutingOption.PopUpTo(
                        routePath = SPLASH_SCREEN_ROUTE_PATH,
                        inclusive = true
                    )
                )
            )

            handleDeepLinks(intent)

            _isInitializationFinished.value = true
        }
    }

    private suspend fun handleDeepLinks(intent: Intent) {
        deepLinkRouters.any { router -> router.handle(intent) }
    }

    companion object {
        private const val INITIALIZATION_DELAY_MILLIS = 1000L
        private const val HANDLE_INITIALIZATION_FINISHED_KEY = "initialization_finished"
    }
}
