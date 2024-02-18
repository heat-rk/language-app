package ru.heatrk.languageapp.di

import androidx.lifecycle.ViewModelProvider
import ru.heatrk.languageapp.core.navigation.api.Router
import scout.Component

object AppComponent : Component(appScope) {
    val router: Router get() = get()

    val mainViewModelFactory: ViewModelProvider.Factory
        get() = get<MainViewModelFactory>().instance
}
