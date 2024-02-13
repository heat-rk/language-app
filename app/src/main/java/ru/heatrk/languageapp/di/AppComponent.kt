package ru.heatrk.languageapp.di

import ru.heatrk.languageapp.core.navigation.api.Router
import scout.Component

object AppComponent : Component(appScope) {
    val router: Router get() = get()
}
