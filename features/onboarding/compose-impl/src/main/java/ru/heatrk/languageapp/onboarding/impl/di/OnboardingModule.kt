package ru.heatrk.languageapp.onboarding.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.impl.data.OnboardingStorage
import ru.heatrk.languageapp.onboarding.impl.domain.OnboardingRepositoryImpl
import ru.heatrk.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingViewModel

val onboardingModule = module {
    single<OnboardingStorage> {
        OnboardingStorage(
            applicationContext = get(),
            dispatcher = get<IoCoroutineDispatcher>().instance
        )
    }

    factory<OnboardingRepository> {
        OnboardingRepositoryImpl(
            onboardingStorage = get()
        )
    }

    single<OnboardingViewModelFactory> {
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
