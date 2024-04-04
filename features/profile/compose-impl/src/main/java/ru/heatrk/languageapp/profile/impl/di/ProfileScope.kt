package ru.heatrk.languageapp.profile.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.profile.api.domain.SettingsRepository
import ru.heatrk.languageapp.profile.impl.data.SettingsRepositoryImpl
import ru.heatrk.languageapp.profile.impl.data.SettingsStorage
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileViewModel
import scout.Scope
import scout.definition.Registry
import scout.scope

private var _profileScope: Scope? = null

internal val profileScope get() = requireNotNull(_profileScope)

fun Scope.includeProfileScope() {
    _profileScope = scope("profile_scope") {
        dependsOn(this@includeProfileScope)
        useProfileScreenBeans()
    }
}

fun Registry.useProfileApiBeans() {
    singleton<SettingsStorage> {
        SettingsStorage(
            applicationContext = get(),
            storageDispatcher = get<IoCoroutineDispatcher>().instance
        )
    }

    reusable<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsStorage = get(),
            applicationContext = get(),
        )
    }
}

private fun Registry.useProfileScreenBeans() {
    singleton<ProfileViewModelFactory> {
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
