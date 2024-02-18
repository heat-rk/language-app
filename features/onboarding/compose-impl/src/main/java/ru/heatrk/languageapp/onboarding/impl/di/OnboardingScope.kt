package ru.heatrk.languageapp.onboarding.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.impl.data.OnboardingStorage
import ru.heatrk.languageapp.onboarding.impl.domain.OnboardingRepositoryImpl
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingViewModel
import scout.Scope
import scout.definition.Registry
import scout.scope

private var _onboardingScope: Scope? = null
internal val onboardingScope get() = requireNotNull(_onboardingScope)

fun Scope.includeOnboardingScope() {
    _onboardingScope = scope("onboarding_scope") {
        dependsOn(this@includeOnboardingScope)

        singleton<OnboardingViewModelFactory> {
            OnboardingViewModelFactory(
                viewModelFactory {
                    initializer {
                        OnboardingViewModel(
                            onboardingRepository = get(),
                            router = get()
                        )
                    }
                }
            )
        }
    }
}

fun Registry.useOnboardingApiBeans() {
    singleton<OnboardingStorage> {
        OnboardingStorage(
            applicationContext = get(),
            dispatcher = get<IoCoroutineDispatcher>().instance
        )
    }

    reusable<OnboardingRepository> {
        OnboardingRepositoryImpl(
            onboardingStorage = get()
        )
    }
}
