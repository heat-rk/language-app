package ru.heatrk.languageapp.core.coroutines.dispatchers.di

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.DefaultCoroutineDispatcher
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.core.coroutines.dispatchers.MainCoroutineDispatcher

val dispatchersModule = module {
    single<MainCoroutineDispatcher> { MainCoroutineDispatcher(Dispatchers.Main) }
    single<IoCoroutineDispatcher> { IoCoroutineDispatcher(Dispatchers.IO) }
    single<DefaultCoroutineDispatcher> { DefaultCoroutineDispatcher(Dispatchers.Default) }
}
