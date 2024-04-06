package ru.heatrk.languageapp.core.profiles.impl.di

import ru.heatrk.languageapp.core.data.cache.InMemoryCacheContainer
import ru.heatrk.languageapp.core.profiles.api.domain.Profile

internal data class ProfileInMemoryCacheContainer(val instance: InMemoryCacheContainer<Profile>)
