package ru.heatrk.languageapp.di

import android.content.Context
import ru.heatrk.languageapp.LanguageApplication
import ru.heatrk.languageapp.auth.impl.di.includeAuthScope
import ru.heatrk.languageapp.auth.impl.di.useAuthApiBeans
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.useDispatchersBeans
import ru.heatrk.languageapp.core.coroutines.scopes.di.useCoroutineScopesBeans
import ru.heatrk.languageapp.core.data.db.di.useDatabaseBeans
import ru.heatrk.languageapp.core.data.http_client.di.useHttpClientBeans
import ru.heatrk.languageapp.core.data.serialization.di.useSerializationBeans
import ru.heatrk.languageapp.core.data.supabase.di.useSupaBaseBeans
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.heatrk.languageapp.core.profiles.impl.di.useProfilesBeans
import ru.heatrk.languageapp.main.impl.di.includeMainScope
import ru.heatrk.languageapp.onboarding.impl.di.includeOnboardingScope
import ru.heatrk.languageapp.onboarding.impl.di.useOnboardingApiBeans
import scout.definition.Registry
import scout.scope

val appScope = scope("app_scope") {
    useApplicationBeans()
    useDispatchersBeans()
    useCoroutineScopesBeans()
    useDatabaseBeans()
    useHttpClientBeans()
    useSerializationBeans()
    useSupaBaseBeans()
    useComposeNavigationBeans()
    useProfilesBeans()

    useOnboardingApiBeans()
    useAuthApiBeans()
}.apply {
    includeOnboardingScope()
    includeAuthScope()
    includeMainScope()
}

private fun Registry.useComposeNavigationBeans() {
    singleton<ComposeRouter> { ComposeRouter() }
    singleton<Router> { get<ComposeRouter>() }
}

private fun Registry.useApplicationBeans() {
    singleton<Context> { LanguageApplication.instance }
}
