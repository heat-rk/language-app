package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.ViewModelProvider
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import scout.Component

object AuthComponent : Component(authScope) {
    val signInViewModelFactory: ViewModelProvider.Factory
        get() = get<SignInViewModelFactory>().instance

    val signUpViewModelFactory: ViewModelProvider.Factory
        get() = get<SignUpViewModelFactory>().instance

    val signUpComposeRouter: ComposeRouter
        get() = get<SignUpComposeRouter>().instance
}
