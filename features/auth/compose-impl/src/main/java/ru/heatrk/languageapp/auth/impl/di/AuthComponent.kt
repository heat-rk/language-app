package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.ViewModelProvider
import ru.heatrk.languageapp.auth.impl.di.recovery.RecoveryComposeRouter
import ru.heatrk.languageapp.auth.impl.di.recovery.RecoveryViewModelFactory
import ru.heatrk.languageapp.auth.impl.di.sign_in.SignInViewModelFactory
import ru.heatrk.languageapp.auth.impl.di.sign_up.SignUpComposeRouter
import ru.heatrk.languageapp.auth.impl.di.sign_up.SignUpViewModelFactory
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import scout.Component

object AuthComponent : Component(authScope) {
    val signInViewModelFactory: ViewModelProvider.Factory
        get() = get<SignInViewModelFactory>().instance

    val signUpViewModelFactory: ViewModelProvider.Factory
        get() = get<SignUpViewModelFactory>().instance

    val recoveryViewModelFactory: ViewModelProvider.Factory
        get() = get<RecoveryViewModelFactory>().instance

    val signUpComposeRouter: ComposeRouter
        get() = get<SignUpComposeRouter>().instance

    val recoveryRouter: ComposeRouter
        get() = get<RecoveryComposeRouter>().instance
}
