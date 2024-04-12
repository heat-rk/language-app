package ru.heatrk.languageapp.core.profiles.impl.di

import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.core.data.cache.InMemoryCacheContainer
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.core.profiles.impl.data.ProfilesRepositoryImpl

val profilesModule = module {
    single<ProfileInMemoryCacheContainer> {
        ProfileInMemoryCacheContainer(
            InMemoryCacheContainer(
                cacheLifeTime = Long.MAX_VALUE
            )
        )
    }

    single<ProfilesRepository> {
        ProfilesRepositoryImpl(
            dispatcher = get<IoCoroutineDispatcher>().instance,
            supabaseClient = get(),
            inMemoryUserProfileCacheContainer = get<ProfileInMemoryCacheContainer>().instance,
            environmentConfig = get(),
        )
    }
}
