package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.auth.impl.ui.login.LoginViewModel
import scout.Scope
import scout.scope

private var _authScope: Scope? = null
internal val authScope get() = requireNotNull(_authScope)

fun Scope.includeAuthScope() {
    _authScope = scope("auth_scope") {
        dependsOn(this@includeAuthScope)

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
