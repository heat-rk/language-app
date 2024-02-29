package ru.heatrk.languageapp.auth.impl.ui.login

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
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.domain.InvalidSignInFieldsValuesException
import ru.heatrk.languageapp.auth.impl.domain.SignInUseCase
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.SideEffect
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.State
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.main.api.MainScreenRoute

typealias IntentBody = SimpleSyntax<State, SideEffect>

class LoginViewModel(
    private val signIn: SignInUseCase,
    private val router: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnEmailTextChanged -> onEmailTextChanged(intent.text)
            is Intent.OnPasswordTextChanged -> onPasswordTextChanged(intent.text)
            Intent.OnForgotPasswordButtonClick -> onForgotPasswordButtonClick()
            Intent.OnGoogleSignInButtonClick -> onGoogleSignInButtonClick()
            Intent.OnLoginButtonClick -> onLoginButtonClick()
            Intent.OnPasswordVisibilityToggleClick -> onPasswordVisibilityToggleClick()
            Intent.OnSignUpButtonClick -> onSignUpButtonClick()
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
        // TODO
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

                router.navigate(MainScreenRoute)
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
        // TODO
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
