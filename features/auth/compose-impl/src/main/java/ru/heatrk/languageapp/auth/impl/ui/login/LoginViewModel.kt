package ru.heatrk.languageapp.auth.impl.ui.login

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.State
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.Intent

typealias IntentBody = SimpleSyntax<State, Unit>

class LoginViewModel : ViewModel(), ContainerHost<State, Unit> {
    override val container = container<State, Unit>(
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
    }

    private suspend fun IntentBody.onEmailTextChanged(text: String) {
        reduce {
            state.copy(
                email = text
            )
        }
    }

    private suspend fun IntentBody.onPasswordTextChanged(text: String) {
        reduce {
            state.copy(
                password = text
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
        // TODO
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
}
