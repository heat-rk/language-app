package ru.heatrk.languageapp.auth.impl.ui.screens.recovery

import ru.heatrk.languageapp.common.utils.StringResource

object RecoveryFlowContract {
    data class State(
        val email: String = "",
        val emailErrorMessage: StringResource? = null,
        val password: String = "",
        val passwordErrorMessage: StringResource? = null,
        val confirmedPassword: String = "",
        val confirmedPasswordErrorMessage: StringResource? = null,
        val isPasswordVisible: Boolean = false,
        val isConfirmedPasswordVisible: Boolean = false,
        val recoveringState: Recovering = Recovering.None,
    ) {
        enum class Recovering {
            None, InProgress, Success, Error
        }
    }

    sealed interface Intent {
        data object OnGoBackClick : Intent
        data object OnPasswordVisibilityToggleClick : Intent
        data object OnConfirmedPasswordVisibilityToggleClick : Intent
        data object OnResetPasswordButtonClick : Intent
        data object OnNewPasswordConfirmButtonClick : Intent
        data object OnCheckEmailOkButtonClick : Intent
        data object OnFinishButtonClick : Intent
        data class OnEmailChanged(val text: String) : Intent
        data class OnPasswordChanged(val text: String) : Intent
        data class OnConfirmedPasswordChanged(val text: String) : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object CloseKeyboard : SideEffect
    }
}
