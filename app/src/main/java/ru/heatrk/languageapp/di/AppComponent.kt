package ru.heatrk.languageapp.di

import androidx.lifecycle.ViewModelProvider
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import scout.Component

object AppComponent : Component(appScope) {
    val router: ComposeRouter get() = get()

    val mainViewModelFactory: ViewModelProvider.Factory
        get() = get<MainViewModelFactory>().instance
}
