package ru.heatrk.languageapp.profile.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.profile.impl.ui.screens.select_language.SelectLanguageViewModel
import scout.Component

object ProfileComponent : Component(profileScope) {
    fun getSelectLanguageViewModelFactory(
        canGoBack: Boolean,
    ) =
        viewModelFactory {
            initializer {
                SelectLanguageViewModel(
                    router = get(),
                    settingsRepository = get(),
                    canGoBack = canGoBack,
                )
            }
        }
}
