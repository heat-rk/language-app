package ru.heatrk.languageapp.auth.impl.ui.sign_up

import ru.heatrk.languageapp.common.utils.StringResource

object SignUpScreenContract {
    sealed interface State {
        data class InputData(
            val inputDataState: State = State.GENERAL,
            val firstName: String = "",
            val firstNameErrorMessage: StringResource? = null,
            val lastName: String = "",
            val lastNameErrorMessage: StringResource? = null,
            val email: String = "",
            val emailErrorMessage: StringResource? = null,
            val password: String = "",
            val passwordErrorMessage: StringResource? = null,
            val confirmedPassword: String = "",
            val confirmedPasswordErrorMessage: StringResource? = null,
            val isPasswordVisible: Boolean = false,
            val isConfirmedPasswordVisible: Boolean = false,
            val registrationState: Registration = Registration.None,
        ) : State {
            enum class Registration {
                None, InProgress, Success, Error
            }

            enum class State {
                GENERAL, PASSWORD
            }
        }

        data object EmailConfirmation : State
    }

    sealed interface Intent {
        data class OnFirstNameChanged(val text: String) : Intent
        data class OnLastNameChanged(val text: String) : Intent
        data class OnEmailChanged(val text: String) : Intent
        data class OnPasswordChanged(val text: String) : Intent
        data class OnConfirmedPasswordChanged(val text: String) : Intent

        data object OnPasswordVisibilityToggleClick : Intent
        data object OnConfirmedPasswordVisibilityToggleClick : Intent
        data object OnSignInButtonClick : Intent
        data object OnSignUpButtonClick : Intent
        data object OnContinueButtonClick : Intent
        data object OnFinishButtonClick : Intent
        data object OnGoBackClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object CloseKeyboard : SideEffect
    }
}
