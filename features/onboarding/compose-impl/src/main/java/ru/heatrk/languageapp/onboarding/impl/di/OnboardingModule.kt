package ru.heatrk.languageapp.onboarding.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.IoCoroutineDispatcherQualifier
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.impl.data.OnboardingStorage
import ru.heatrk.languageapp.onboarding.impl.domain.OnboardingRepositoryImpl
import ru.heatrk.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingViewModel

internal val OnboardingViewModelFactoryQualifier =
    qualifier("OnboardingViewModelFactory")

val onboardingModule = module {
    single<OnboardingStorage> {
        OnboardingStorage(
            applicationContext = get(),
            dispatcher = get(IoCoroutineDispatcherQualifier)
        )
    }

    factory<OnboardingRepository> {
        OnboardingRepositoryImpl(
            onboardingStorage = get()
        )
    }

    single<ViewModelProvider.Factory>(OnboardingViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                OnboardingViewModel(
                    onboardingRepository = get(),
                    router = get()
                )
            }
        }
    }
}
