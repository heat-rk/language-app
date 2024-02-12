package ru.heatrk.languageapp.feature.splash.impl.di

import androidx.lifecycle.ViewModelProvider
import scout.Component

object SplashComponent : Component(splashScope) {
    val splashViewModelFactory: ViewModelProvider.Factory
        get() = get<SplashViewModelFactory>().instance
}