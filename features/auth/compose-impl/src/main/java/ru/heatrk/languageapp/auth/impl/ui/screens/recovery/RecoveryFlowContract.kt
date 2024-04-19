package ru.heatrk.languageapp.auth.impl.ui.screens.recovery

import ru.heatrk.languageapp.common.utils.StringResource
import ru.heatrk.languageapp.common.utils.states.ProcessingState

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
        val isRecoveryCodeHandled: Boolean = false,
        val recoveringState: ProcessingState = ProcessingState.None,
    )

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
        data class OnRecoveryCodeReceived(val code: String?) : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
        data object CloseKeyboard : SideEffect
    }
}
