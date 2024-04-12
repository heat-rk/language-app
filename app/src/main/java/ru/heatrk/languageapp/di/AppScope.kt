package ru.heatrk.languageapp.di

import android.app.Application
import ru.heatrk.languageapp.LanguageApplication
import ru.heatrk.languageapp.auth.impl.di.includeAuthScope
import ru.heatrk.languageapp.auth.impl.di.useAuthApiBeans
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.useDispatchersBeans
import ru.heatrk.languageapp.core.coroutines.scopes.di.useCoroutineScopesBeans
import ru.heatrk.languageapp.core.data.http_client.di.useHttpClientBeans
import ru.heatrk.languageapp.core.data.serialization.di.useSerializationBeans
import ru.heatrk.languageapp.core.data.supabase.di.useSupaBaseBeans
import ru.heatrk.languageapp.core.di.useAndroidLoggerBeans
import ru.heatrk.languageapp.core.env.di.useAndroidEnvironmentConfig
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.heatrk.languageapp.core.profiles.impl.di.useProfilesBeans
import ru.heatrk.languageapp.exercises.guess_animal.impl.di.includeGuessAnimalScope
import ru.heatrk.languageapp.exercises.word_practice.impl.di.includeWordPracticeScope
import ru.heatrk.languageapp.main.impl.di.includeMainScope
import ru.heatrk.languageapp.onboarding.impl.di.includeOnboardingScope
import ru.heatrk.languageapp.onboarding.impl.di.useOnboardingApiBeans
import ru.heatrk.languageapp.profile.impl.di.includeProfileScope
import ru.heatrk.languageapp.profile.impl.di.useProfileApiBeans
import scout.definition.Registry
import scout.scope

val appScope = scope("app_scope") {
    useApplicationBeans()
    useDispatchersBeans()
    useCoroutineScopesBeans()
    useHttpClientBeans()
    useSerializationBeans()
    useSupaBaseBeans()
    useComposeNavigationBeans()
    useProfilesBeans()
    useAndroidLoggerBeans()
    useAndroidEnvironmentConfig()

    useOnboardingApiBeans()
    useAuthApiBeans()
    useProfileApiBeans()
}.apply {
    includeOnboardingScope()
    includeAuthScope()
    includeMainScope()
    includeProfileScope()
    includeGuessAnimalScope()
    includeWordPracticeScope()
}

private fun Registry.useComposeNavigationBeans() {
    singleton<ComposeRouter> { ComposeRouter() }
    singleton<Router> { get<ComposeRouter>() }
}

private fun Registry.useApplicationBeans() {
    singleton<Application> { LanguageApplication.instance }
}
