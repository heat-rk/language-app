package ru.heatrk.languageapp.login.impl.di

import androidx.lifecycle.ViewModelProvider
import scout.Component

object LoginComponent : Component(loginScope) {
    val loginViewModelFactory: ViewModelProvider.Factory
        get() = get<LoginViewModelFactory>().instance
}
