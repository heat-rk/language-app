package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.api.domain.google.AuthGoogleNonceProvider
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

val authModule = module {
    useDataBeans()
    useUseCasesBeans()
    useViewModelFactoriesBeans()
    useAuthBeans()
    useNavigationBeans()
}

private fun Module.useNavigationBeans() {
    single<RecoveryComposeRouter> { RecoveryComposeRouter(ComposeRouter()) }
    single<RecoveryRouter> { RecoveryRouter(instance = get<RecoveryComposeRouter>().instance) }

    single<SignUpComposeRouter> { SignUpComposeRouter(ComposeRouter()) }
    single<SignUpRouter> { SignUpRouter(instance = get<SignUpComposeRouter>().instance) }

    single<DeepLinkRouter> {
        AuthDeepLinkRouter(
            router = get(),
            recoveryRouter = get<RecoveryRouter>().instance,
            authRepository = get()
        )
    }
}

private fun Module.useAuthBeans() {
    factory<AuthGoogleNonceProvider> {
        AuthGoogleNonceProviderImpl(
            dispatcher = get<DefaultCoroutineDispatcher>().instance,
        )
    }
}

private fun Module.useViewModelFactoriesBeans() {
    single<SignInViewModelFactory> {
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

    single<SignUpViewModelFactory> {
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

    single<RecoveryViewModelFactory> {
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

private fun Module.useUseCasesBeans() {
    factory<PasswordValidator> { PasswordValidator() }

    factory<SignInUseCase> {
        SignInUseCase(
            authRepository = get(),
            profilesRepository = get(),
        )
    }

    factory<SignInWithGoogleUseCase> {
        SignInWithGoogleUseCase(
            authRepository = get(),
            profilesRepository = get(),
        )
    }

    factory<SignUpUseCase> {
        SignUpUseCase(
            repository = get(),
            validatePassword = get()
        )
    }

    factory<RecoveryUseCase> {
        RecoveryUseCase(
            repository = get(),
            validatePassword = get()
        )
    }
}

private fun Module.useDataBeans() {
    single<AuthStorage> {
        AuthStorage(
            storageDispatcher = get<IoCoroutineDispatcher>().instance,
            applicationContext = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            supabaseClient = get(),
            authStorage = get(),
            json = get(),
            supabaseDispatcher = get<IoCoroutineDispatcher>().instance,
            environmentConfig = get(),
        )
    }
}
