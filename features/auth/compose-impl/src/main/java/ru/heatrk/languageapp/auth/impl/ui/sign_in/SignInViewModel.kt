package ru.heatrk.languageapp.auth.impl.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.exceptions.BadRequestRestException
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.auth.api.ui.navigation.SignUpScreenRoute
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonceProvider
import ru.heatrk.languageapp.auth.impl.domain.sign_in.InvalidSignInFieldsValuesException
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInUseCase
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInWithGoogleUseCase
import ru.heatrk.languageapp.auth.impl.ui.sign_in.SignInScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.sign_in.SignInScreenContract.SideEffect
import ru.heatrk.languageapp.auth.impl.ui.sign_in.SignInScreenContract.State
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOptions
import ru.heatrk.languageapp.main.api.MainScreenRoute

typealias IntentBody = SimpleSyntax<State, SideEffect>

class SignInViewModel(
    private val signIn: SignInUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val authGoogleNonceProvider: AuthGoogleNonceProvider,
    private val router: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnEmailTextChanged ->
                onEmailTextChanged(intent.text)
            is Intent.OnPasswordTextChanged ->
                onPasswordTextChanged(intent.text)
            is Intent.OnGoogleCredentialsReceived ->
                onGoogleCredentialsReceived(
                    rawNonce = intent.rawNonce,
                    idToken = intent.idToken,
                    email = intent.email,
                    firstName = intent.firstName,
                    lastName = intent.lastName
                )
            Intent.OnForgotPasswordButtonClick ->
                onForgotPasswordButtonClick()
            Intent.OnGoogleSignInButtonClick ->
                onGoogleSignInButtonClick()
            Intent.OnLoginButtonClick ->
                onLoginButtonClick()
            Intent.OnPasswordVisibilityToggleClick ->
                onPasswordVisibilityToggleClick()
            Intent.OnSignUpButtonClick ->
                onSignUpButtonClick()
            Intent.OnGoogleCredentialsReceiveFailed ->
                onGoogleCredentialsReceiveFailed()
        }

        processKeyboardClose(intent)
    }

    private suspend fun IntentBody.processKeyboardClose(intent: Intent) {
        when (intent) {
            is Intent.OnForgotPasswordButtonClick,
            Intent.OnGoogleSignInButtonClick,
            Intent.OnLoginButtonClick,
            Intent.OnSignUpButtonClick -> {
                postSideEffect(SideEffect.CloseKeyboard)
            }
            else -> Unit
        }
    }

    private suspend fun IntentBody.onEmailTextChanged(text: String) {
        reduce {
            state.copy(
                email = text,
                emailErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onPasswordTextChanged(text: String) {
        reduce {
            state.copy(
                password = text,
                passwordErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onForgotPasswordButtonClick() {
        // TODO
    }

    private suspend fun IntentBody.onGoogleSignInButtonClick() {
        postSideEffect(SideEffect.RequestGoogleCredentials(
            nonce = authGoogleNonceProvider.provideNonce()
        ))
    }

    private suspend fun IntentBody.onGoogleCredentialsReceived(
        rawNonce: String,
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
    ) {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(authorizingState = State.Authorizing.InProgress) }

                signInWithGoogle(
                    rawNonce = rawNonce,
                    idToken = idToken,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                )

                reduce { state.copy(authorizingState = State.Authorizing.Success) }

                delay(AUTHORIZING_STATE_DELAY_MILLIS)

                router.navigate(
                    route = MainScreenRoute,
                    options = RoutingOptions(
                        shouldBePopUp = true
                    )
                )
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))
            }
        )
    }

    private suspend fun IntentBody.onGoogleCredentialsReceiveFailed() {
        postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))
    }

    private suspend fun IntentBody.onLoginButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(authorizingState = State.Authorizing.InProgress) }

                signIn(
                    email = state.email,
                    password = state.password
                )

                reduce { state.copy(authorizingState = State.Authorizing.Success) }

                delay(AUTHORIZING_STATE_DELAY_MILLIS)

                router.navigate(
                    route = MainScreenRoute,
                    options = RoutingOptions(
                        shouldBePopUp = true
                    )
                )
            },
            onError = { throwable ->
                reduce { state.copy(authorizingState = State.Authorizing.Error) }

                when (throwable) {
                    is InvalidSignInFieldsValuesException -> {
                        reduce {
                            state.copy(
                                emailErrorMessage = throwable.emailError.toStringResource(),
                                passwordErrorMessage = throwable.passwordError.toStringResource(),
                            )
                        }
                    }

                    is BadRequestRestException -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_check_your_creds)))
                    }

                    else -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))
                    }
                }

                delay(AUTHORIZING_STATE_DELAY_MILLIS)

                reduce { state.copy(authorizingState = State.Authorizing.None) }
            }
        )
    }

    private suspend fun IntentBody.onSignUpButtonClick() {
        router.navigate(SignUpScreenRoute)
    }

    private suspend fun IntentBody.onPasswordVisibilityToggleClick() {
        reduce {
            state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    private fun InvalidSignInFieldsValuesException.Email?.toStringResource() = when (this) {
        InvalidSignInFieldsValuesException.Email.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }

    private fun InvalidSignInFieldsValuesException.Password?.toStringResource() = when (this) {
        InvalidSignInFieldsValuesException.Password.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }

    companion object {
        private const val AUTHORIZING_STATE_DELAY_MILLIS = 1000L
    }
}
