package ru.heatrk.languageapp.main.impl.di

import androidx.lifecycle.ViewModelProvider
import scout.Component

object MainComponent : Component(mainScope) {
    val mainViewModelFactory: ViewModelProvider.Factory
        get() = get<MainViewModelFactory>().instance
}
