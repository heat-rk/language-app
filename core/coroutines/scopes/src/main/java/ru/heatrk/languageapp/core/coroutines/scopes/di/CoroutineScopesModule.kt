package ru.heatrk.languageapp.core.coroutines.scopes.di

import kotlinx.coroutines.MainScope
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.scopes.LongRunningCoroutineScope

val coroutineScopesModule = module {
    single<LongRunningCoroutineScope> { LongRunningCoroutineScope(MainScope()) }
}
