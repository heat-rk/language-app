package ru.heatrk.languageapp.core.profiles.impl.di

import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.core.profiles.impl.data.ProfilesRepositoryImpl
import scout.definition.Registry

fun Registry.useProfilesBeans() {
    reusable<ProfilesRepository> {
        ProfilesRepositoryImpl(
            dispatcher = get<IoCoroutineDispatcher>().instance,
            supabaseClient = get(),
        )
    }
}
