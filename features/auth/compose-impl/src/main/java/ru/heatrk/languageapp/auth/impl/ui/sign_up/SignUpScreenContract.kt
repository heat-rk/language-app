package ru.heatrk.languageapp.auth.impl.ui.sign_up

object SignUpScreenContract {
    data class State(
        val inputDataState: InputDataState = InputDataState.GENERAL,
        val firstName: String = "",
        val firstNameErrorMessage: String? = null,
        val lastName: String = "",
        val lastNameErrorMessage: String? = null,
        val email: String = "",
        val emailErrorMessage: String? = null,
        val password: String = "",
        val passwordErrorMessage: String? = null,
        val confirmedPassword: String = "",
        val confirmedPasswordErrorMessage: String? = null,
        val isPasswordVisible: Boolean = false,
        val isConfirmedPasswordVisible: Boolean = false,
        val registrationState: Registration = Registration.None,
    ) {
        enum class Registration {
            None, InProgress, Success, Error
        }

        enum class InputDataState {
            GENERAL, PASSWORD
        }
    }

    sealed interface Intent {
        data class OnFirstNameChanged(val text: String) : Intent
        data class OnLastNameChanged(val text: String) : Intent
        data class OnEmailChanged(val text: String) : Intent
        data class OnPasswordChanged(val text: String) : Intent
        data class OnConfirmedPasswordChanged(val text: String) : Intent

        data object OnPasswordVisibilityToggleClick : Intent
        data object OnConfirmedPasswordVisibilityToggleClick : Intent
        data object OnRegisterButtonClick : Intent
        data object OnSignInButtonClick : Intent
        data object OnSignUpButtonClick : Intent
        data object OnContinueButtonClick : Intent
        data object OnGoBackClick : Intent
    }
}
