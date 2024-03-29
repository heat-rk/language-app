package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.impl.data.AuthRepositoryImpl
import ru.heatrk.languageapp.auth.impl.data.AuthStorage
import ru.heatrk.languageapp.auth.impl.data.google.AuthGoogleNonceProviderImpl
import ru.heatrk.languageapp.auth.impl.di.recovery.RecoveryComposeRouter
import ru.heatrk.languageapp.auth.impl.di.recovery.RecoveryRouter
import ru.heatrk.languageapp.auth.impl.di.recovery.RecoveryViewModelFactory
import ru.heatrk.languageapp.auth.impl.di.sign_in.SignInViewModelFactory
import ru.heatrk.languageapp.auth.impl.di.sign_up.SignUpComposeRouter
import ru.heatrk.languageapp.auth.impl.di.sign_up.SignUpRouter
import ru.heatrk.languageapp.auth.impl.di.sign_up.SignUpViewModelFactory
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonceProvider
import ru.heatrk.languageapp.auth.impl.domain.password_validator.PasswordValidator
import ru.heatrk.languageapp.auth.impl.domain.recovery.RecoveryUseCase
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInUseCase
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInWithGoogleUseCase
import ru.heatrk.languageapp.auth.impl.domain.sign_up.SignUpUseCase
import ru.heatrk.languageapp.auth.impl.ui.navigation.AuthDeepLinkRouter
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_in.SignInViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.heatrk.languageapp.core.coroutines.dispatchers.DefaultCoroutineDispatcher
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.core.navigation.api.DeepLinkRouter
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
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
        useNavigationBeans()
    }
}

fun ScopeBuilder.useAuthApiBeans() {
    singleton<RecoveryComposeRouter> { RecoveryComposeRouter(ComposeRouter()) }
    singleton<RecoveryRouter> { RecoveryRouter(instance = get<RecoveryComposeRouter>().instance) }

    singleton<AuthStorage> {
        AuthStorage(
            storageDispatcher = get<IoCoroutineDispatcher>().instance,
            applicationContext = get()
        )
    }

    reusable<AuthRepository> {
        AuthRepositoryImpl(
            supabaseClient = get(),
            authStorage = get(),
            json = get(),
            supabaseDispatcher = get<IoCoroutineDispatcher>().instance
        )
    }

    element<DeepLinkRouter> {
        AuthDeepLinkRouter(
            router = get(),
            recoveryRouter = get<RecoveryRouter>().instance,
            authRepository = get()
        )
    }
}

private fun ScopeBuilder.useNavigationBeans() {
    singleton<SignUpComposeRouter> { SignUpComposeRouter(ComposeRouter()) }
    singleton<SignUpRouter> { SignUpRouter(instance = get<SignUpComposeRouter>().instance) }
}

private fun ScopeBuilder.useAuthBeans() {
    reusable<AuthGoogleNonceProvider> {
        AuthGoogleNonceProviderImpl(
            dispatcher = get<DefaultCoroutineDispatcher>().instance,
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
                        signUpRouter = get<SignUpRouter>().instance,
                        signUp = get()
                    )
                }
            }
        )
    }

    singleton<RecoveryViewModelFactory> {
        RecoveryViewModelFactory(
            viewModelFactory {
                initializer {
                    RecoveryFlowViewModel(
                        resetPassword = get(),
                        router = get(),
                        recoveryRouter = get<RecoveryRouter>().instance
                    )
                }
            }
        )
    }
}

private fun ScopeBuilder.useUseCasesBeans() {
    reusable<PasswordValidator> { PasswordValidator() }

    reusable<SignInUseCase> {
        SignInUseCase(
            authRepository = get(),
            profilesRepository = get(),
        )
    }

    reusable<SignInWithGoogleUseCase> {
        SignInWithGoogleUseCase(
            authRepository = get(),
            profilesRepository = get(),
        )
    }

    reusable<SignUpUseCase> {
        SignUpUseCase(
            repository = get(),
            validatePassword = get()
        )
    }

    reusable<RecoveryUseCase> {
        RecoveryUseCase(
            repository = get(),
            validatePassword = get()
        )
    }
}
