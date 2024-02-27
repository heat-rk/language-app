package ru.heatrk.languageapp.auth.impl.ui.login

object LoginScreenContract {
    data class State(
        val email: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false,
    )

    sealed interface Intent {
        data class OnEmailTextChanged(val text: String) : Intent
        data class OnPasswordTextChanged(val text: String) : Intent

        data object OnPasswordVisibilityToggleClick : Intent
        data object OnForgotPasswordButtonClick : Intent
        data object OnLoginButtonClick : Intent
        data object OnSignUpButtonClick : Intent
        data object OnGoogleSignInButtonClick : Intent
    }
}
