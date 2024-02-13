package ru.heatrk.languageapp.di

import android.content.Context
import ru.heatrk.languageapp.LanguageApplication
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.useDispatchersBeans
import ru.heatrk.languageapp.core.coroutines.scopes.di.useCoroutineScopesBeans
import ru.heatrk.languageapp.core.data.db.di.useDatabaseBeans
import ru.heatrk.languageapp.core.data.http_client.di.useHttpClientBeans
import ru.heatrk.languageapp.core.navigation.compose_impl.di.useComposeNavigationBeans
import ru.heatrk.languageapp.onboarding.impl.di.includeOnboardingScope
import scout.definition.Registry
import scout.scope

val appScope = scope("app_scope") {
    useApplicationBeans()
    useDispatchersBeans()
    useCoroutineScopesBeans()
    useDatabaseBeans()
    useHttpClientBeans()
    useComposeNavigationBeans()
}.apply {
    includeOnboardingScope()
}

private fun Registry.useApplicationBeans() {
    singleton<Context> { LanguageApplication.instance }
}
