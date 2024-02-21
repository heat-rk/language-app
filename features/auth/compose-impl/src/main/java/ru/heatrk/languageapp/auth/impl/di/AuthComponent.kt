package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.ViewModelProvider
import scout.Component

object AuthComponent : Component(authScope) {
    val loginViewModelFactory: ViewModelProvider.Factory
        get() = get<LoginViewModelFactory>().instance
}
