package ru.heatrk.languageapp.core.coroutines.scopes.di

import kotlinx.coroutines.MainScope
import ru.heatrk.languageapp.core.coroutines.scopes.LongRunningCoroutineScope
import scout.definition.Registry

fun Registry.useCoroutineScopesBeans() {
    singleton<LongRunningCoroutineScope> { LongRunningCoroutineScope(MainScope()) }
}