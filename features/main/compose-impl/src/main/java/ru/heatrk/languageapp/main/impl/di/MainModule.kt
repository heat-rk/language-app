package ru.heatrk.languageapp.main.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.dsl.module
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainViewModel

val mainModule = module {
    single<MainViewModelFactory> {
        MainViewModelFactory(
            viewModelFactory {
                initializer {
                    MainViewModel(
                        profilesRepository = get(),
                        settingsRepository = get(),
                        router = get(),
                    )
                }
            }
        )
    }
}
