package ru.heatrk.languageapp.di

import android.content.Intent
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.heatrk.languageapp.presentation.InitializationViewModel
import ru.heatrk.languageapp.presentation.ThemeViewModel
import scout.Component

object AppComponent : Component(appScope) {
    val router: ComposeRouter get() = get()

    fun getRootViewModelFactory(intent: Intent) =
        viewModelFactory {
            initializer {
                InitializationViewModel(
                    onboardingRepository = get(),
                    authRepository = get(),
                    router = get(),
                    deepLinkRouters = collect(),
                    savedStateHandle = createSavedStateHandle(),
                    intent = intent,
                )
            }
        }

    fun getThemeViewModelFactory() =
        viewModelFactory {
            initializer {
                ThemeViewModel(
                    settingsRepository = get(),
                )
            }
        }
}
