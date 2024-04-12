package ru.heatrk.languageapp.di

import org.koin.dsl.module
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter

val composeNavigationModule = module {
    single<ComposeRouter> { ComposeRouter() }
    single<Router> { get<ComposeRouter>() }
}
