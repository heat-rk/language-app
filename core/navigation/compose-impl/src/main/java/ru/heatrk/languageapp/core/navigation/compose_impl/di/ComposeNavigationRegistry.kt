package ru.heatrk.languageapp.core.navigation.compose_impl.di

import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import scout.definition.Registry

fun Registry.useComposeNavigationBeans() {
    singleton<Router> { ComposeRouter() }
}
