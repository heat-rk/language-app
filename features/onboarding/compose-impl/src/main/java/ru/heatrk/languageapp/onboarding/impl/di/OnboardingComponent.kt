package ru.heatrk.languageapp.onboarding.impl.di

import androidx.lifecycle.ViewModelProvider
import scout.Component

object OnboardingComponent : Component(onboardingScope) {
    val onboardingViewModelFactory: ViewModelProvider.Factory
        get() = get<OnboardingViewModelFactory>().instance
}
