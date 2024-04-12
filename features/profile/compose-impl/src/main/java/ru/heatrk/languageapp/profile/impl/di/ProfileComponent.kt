package ru.heatrk.languageapp.profile.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.core.env.EnvironmentConfig
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropViewModel
import ru.heatrk.languageapp.profile.impl.ui.screens.select_language.SelectLanguageViewModel
import scout.Component

object ProfileComponent : Component(profileScope) {

    val environmentConfig: EnvironmentConfig
        get() = get()

    val profileViewModelFactory: ViewModelProvider.Factory
        get() = get<ProfileViewModelFactory>().instance

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

    fun getAvatarCropViewModelFactory(
        photoUri: String,
    ) =
        viewModelFactory {
            initializer {
                AvatarCropViewModel(
                    photoUri = photoUri,
                    router = get(),
                    croppedAvatarUpload = get(),
                )
            }
        }
}
