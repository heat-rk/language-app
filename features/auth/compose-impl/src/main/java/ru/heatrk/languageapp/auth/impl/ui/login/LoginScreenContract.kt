package ru.heatrk.languageapp.auth.impl.ui.login

import ru.heatrk.languageapp.common.utils.StringResource

object LoginScreenContract {
    data class State(
        val email: String = "",
        val emailErrorMessage: StringResource? = null,
        val password: String = "",
        val passwordErrorMessage: StringResource? = null,
        val isPasswordVisible: Boolean = false,
        val authorizingState: Authorizing = Authorizing.None,
    ) {
        enum class Authorizing {
            None, InProgress, Success, Error
        }
    }

    sealed interface Intent {
        data class OnEmailTextChanged(val text: String) : Intent
        data class OnPasswordTextChanged(val text: String) : Intent

        data object OnPasswordVisibilityToggleClick : Intent
        data object OnForgotPasswordButtonClick : Intent
        data object OnLoginButtonClick : Intent
        data object OnSignUpButtonClick : Intent
        data object OnGoogleSignInButtonClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object CloseKeyboard : SideEffect
    }
}
