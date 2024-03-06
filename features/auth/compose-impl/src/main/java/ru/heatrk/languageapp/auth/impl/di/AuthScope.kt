package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.impl.data.AuthRepositoryImpl
import ru.heatrk.languageapp.auth.impl.data.google.AuthGoogleNonceProviderImpl
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonceProvider
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInUseCase
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInWithGoogleUseCase
import ru.heatrk.languageapp.auth.impl.domain.sign_up.SignUpUseCase
import ru.heatrk.languageapp.auth.impl.ui.sign_in.SignInViewModel
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpViewModel
import ru.heatrk.languageapp.core.coroutines.dispatchers.DefaultCoroutineDispatcher
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import scout.Scope
import scout.scope
import scout.scope.builder.ScopeBuilder

private var _authScope: Scope? = null
internal val authScope get() = requireNotNull(_authScope)

fun Scope.includeAuthScope() {
    _authScope = scope("auth_scope") {
        dependsOn(this@includeAuthScope)

        useUseCasesBeans()
        useViewModelFactoriesBeans()
        useAuthBeans()
    }
}

private fun ScopeBuilder.useAuthBeans() {
    reusable<AuthGoogleNonceProvider> {
        AuthGoogleNonceProviderImpl(
            dispatcher = get<DefaultCoroutineDispatcher>().instance,
        )
    }

    reusable<AuthRepository> {
        AuthRepositoryImpl(
            supabaseClient = get(),
            supabaseDispatcher = get<IoCoroutineDispatcher>().instance
        )
    }
}

private fun ScopeBuilder.useViewModelFactoriesBeans() {
    singleton<SignInViewModelFactory> {
        SignInViewModelFactory(
            viewModelFactory {
                initializer {
                    SignInViewModel(
                        signIn = get(),
                        signInWithGoogle = get(),
                        authGoogleNonceProvider = get(),
                        router = get(),
                    )
                }
            }
        )
    }

    singleton<SignUpViewModelFactory> {
        SignUpViewModelFactory(
            viewModelFactory {
                initializer {
                    SignUpViewModel(
                        router = get(),
                        signUp = get()
                    )
                }
            }
        )
    }
}

private fun ScopeBuilder.useUseCasesBeans() {
    reusable<SignInUseCase> {
        SignInUseCase(
            repository = get()
        )
    }

    reusable<SignInWithGoogleUseCase> {
        SignInWithGoogleUseCase(
            repository = get()
        )
    }

    reusable<SignUpUseCase> {
        SignUpUseCase(
            repository = get()
        )
    }
}
