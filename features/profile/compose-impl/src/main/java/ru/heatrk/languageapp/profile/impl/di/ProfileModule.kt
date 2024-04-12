package ru.heatrk.languageapp.profile.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.DefaultCoroutineDispatcherQualifier
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.IoCoroutineDispatcherQualifier
import ru.heatrk.languageapp.profile.api.domain.SettingsRepository
import ru.heatrk.languageapp.profile.impl.data.AvatarCropperImpl
import ru.heatrk.languageapp.profile.impl.data.SettingsRepositoryImpl
import ru.heatrk.languageapp.profile.impl.data.SettingsStorage
import ru.heatrk.languageapp.profile.impl.domain.AvatarCropper
import ru.heatrk.languageapp.profile.impl.domain.CroppedAvatarUploadUseCase
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileViewModel

val profileModule = module {
    single<SettingsStorage> {
        SettingsStorage(
            applicationContext = get(),
            storageDispatcher = get(IoCoroutineDispatcherQualifier)
        )
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsStorage = get(),
            applicationContext = get(),
        )
    }

    useProfileScreenBeans()
    useAvatarCropperBeans()
}

private fun Module.useProfileScreenBeans() {
    single<ProfileViewModelFactory> {
        ProfileViewModelFactory(
            viewModelFactory {
                initializer {
                    ProfileViewModel(
                        router = get(),
                        profilesRepository = get(),
                        settingsRepository = get(),
                        authRepository = get(),
                    )
                }
            }
        )
    }
}

private fun Module.useAvatarCropperBeans() {
    factory<AvatarCropper> {
        AvatarCropperImpl(
            cropperDispatcher = get(DefaultCoroutineDispatcherQualifier),
            applicationContext = get(),
        )
    }

    factory<CroppedAvatarUploadUseCase> {
        CroppedAvatarUploadUseCase(
            profilesRepository = get(),
            avatarCropper = get(),
        )
    }
}
