package ru.heatrk.languageapp.main.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainViewModel
import scout.Scope
import scout.scope

private var _mainScope: Scope? = null
internal val mainScope get() = requireNotNull(_mainScope)

fun Scope.includeMainScope() {
    _mainScope = scope("main_scope") {
        dependsOn(this@includeMainScope)

        singleton<MainViewModelFactory> {
            MainViewModelFactory(
                viewModelFactory {
                    initializer {
                        MainViewModel()
                    }
                }
            )
        }
    }
}
