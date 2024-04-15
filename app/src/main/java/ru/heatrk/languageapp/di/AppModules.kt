package ru.heatrk.languageapp.di

import ru.heatrk.languageapp.audition.word_practice.impl.di.auditionModule
import ru.heatrk.languageapp.auth.impl.di.authModule
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.dispatchersModule
import ru.heatrk.languageapp.core.coroutines.scopes.di.coroutineScopesModule
import ru.heatrk.languageapp.core.data.http_client.di.httpClientModule
import ru.heatrk.languageapp.core.data.serialization.di.serializationModule
import ru.heatrk.languageapp.core.data.supabase.di.supabaseModule
import ru.heatrk.languageapp.core.di.androidLoggerModule
import ru.heatrk.languageapp.core.env.di.androidEnvironmentConfigModule
import ru.heatrk.languageapp.core.profiles.impl.di.profilesModule
import ru.heatrk.languageapp.exercises.guess_animal.impl.di.guessAnimalModule
import ru.heatrk.languageapp.exercises.word_practice.impl.di.wordPracticeModule
import ru.heatrk.languageapp.main.impl.di.mainModule
import ru.heatrk.languageapp.onboarding.impl.di.onboardingModule
import ru.heatrk.languageapp.profile.impl.di.profileModule

val appModules = listOf(
    appModule,
    dispatchersModule,
    coroutineScopesModule,
    httpClientModule,
    serializationModule,
    supabaseModule,
    composeNavigationModule,
    profilesModule,
    androidLoggerModule,
    androidEnvironmentConfigModule,
    onboardingModule,
    authModule,
    mainModule,
    profileModule,
    guessAnimalModule,
    wordPracticeModule,
    auditionModule,
)
