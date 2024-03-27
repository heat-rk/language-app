package ru.heatrk.languageapp.di

import android.content.Intent
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.heatrk.languageapp.presentation.MainViewModel
import scout.Component

object AppComponent : Component(appScope) {
    val router: ComposeRouter get() = get()

    fun getMainViewModelFactory(intent: Intent) =
        viewModelFactory {
            initializer {
                MainViewModel(
                    onboardingRepository = get(),
                    authRepository = get(),
                    router = get(),
                    deepLinkRouters = collect(),
                    intent = intent,
                )
            }
        }
}
