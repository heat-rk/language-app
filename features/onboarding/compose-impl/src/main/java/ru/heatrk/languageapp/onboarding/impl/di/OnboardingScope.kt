package ru.heatrk.languageapp.onboarding.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingViewModel
import scout.Scope
import scout.scope

private var _onboardingScope: Scope? = null
internal val onboardingScope get() = requireNotNull(_onboardingScope)

fun Scope.includeOnboardingScope() {
    _onboardingScope = scope("splash_scope") {
        dependsOn(this@includeOnboardingScope)

        singleton<OnboardingViewModelFactory> {
            OnboardingViewModelFactory(
                viewModelFactory {
                    initializer {
                        OnboardingViewModel()
                    }
                }
            )
        }
    }
}
