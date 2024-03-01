package ru.heatrk.languageapp.auth.impl.ui.sign_in

import androidx.credentials.CredentialManager
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonce
import ru.heatrk.languageapp.common.utils.StringResource

object SignInScreenContract {
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
        data class OnGoogleCredentialsReceived(
            val rawNonce: String,
            val idToken: String,
        ) : Intent

        data object OnPasswordVisibilityToggleClick : Intent
        data object OnForgotPasswordButtonClick : Intent
        data object OnLoginButtonClick : Intent
        data object OnSignUpButtonClick : Intent
        data object OnGoogleSignInButtonClick : Intent
        data object OnGoogleCredentialsReceiveFailed : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect

        data class RequestGoogleCredentials(
            val nonce: AuthGoogleNonce,
            val credentialManager: CredentialManager,
        ) : SideEffect

        data object CloseKeyboard : SideEffect
    }
}
