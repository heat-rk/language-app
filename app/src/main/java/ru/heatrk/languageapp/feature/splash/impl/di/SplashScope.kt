package ru.heatrk.languageapp.feature.splash.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.di.appScope
import ru.heatrk.languageapp.feature.splash.impl.ui.SplashViewModel
import scout.scope

val splashScope = scope("splash_scope") {
    dependsOn(appScope)

    singleton<SplashViewModelFactory> {
        SplashViewModelFactory(
            viewModelFactory {
                initializer {
                    SplashViewModel()
                }
            }
        )
    }
}
