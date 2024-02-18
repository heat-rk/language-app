package ru.heatrk.languageapp.login.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.login.impl.ui.LoginViewModel
import scout.Scope
import scout.scope

private var _loginScope: Scope? = null
internal val loginScope get() = requireNotNull(_loginScope)

fun Scope.includeLoginScope() {
    _loginScope = scope("login_scope") {
        dependsOn(this@includeLoginScope)

        singleton<LoginViewModelFactory> {
            LoginViewModelFactory(
                viewModelFactory {
                    initializer {
                        LoginViewModel()
                    }
                }
            )
        }
    }
}
