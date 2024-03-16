package ru.heatrk.languageapp.auth.impl.ui.screens.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.domain.recovery.InvalidRecoveryFieldsValuesException
import ru.heatrk.languageapp.auth.impl.domain.recovery.RecoveryUseCase
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_CHECK_EMAIL_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_SUCCESS_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.SideEffect
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOption

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class RecoveryFlowViewModel(
    private val resetPassword: RecoveryUseCase,
    private val router: Router,
    private val recoveryRouter: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnEmailChanged ->
                onEmailChanged(intent.text)
            is Intent.OnPasswordChanged ->
                onPasswordChanged(intent.text)
            is Intent.OnConfirmedPasswordChanged ->
                onConfirmedPasswordChanged(intent.text)
            Intent.OnPasswordVisibilityToggleClick ->
                onPasswordVisibilityToggleClick()
            Intent.OnConfirmedPasswordVisibilityToggleClick ->
                onConfirmedPasswordVisibilityToggleClick()
            Intent.OnNewPasswordConfirmButtonClick ->
                onConfirmNewPasswordButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnResetPasswordButtonClick ->
                onResetPasswordButtonClick()
            Intent.OnCheckEmailOkButtonClick ->
                onCheckEmailOkButtonClick()
            Intent.OnFinishButtonClick ->
                onFinishButtonClick()
        }

        processKeyboardClose(intent)
    }

    private suspend fun IntentBody.processKeyboardClose(intent: Intent) {
        when (intent) {
            is Intent.OnCheckEmailOkButtonClick,
            Intent.OnResetPasswordButtonClick,
            Intent.OnGoBackClick,
            Intent.OnNewPasswordConfirmButtonClick,-> {
                postSideEffect(SideEffect.CloseKeyboard)
            }
            else -> Unit
        }
    }

    private suspend fun IntentBody.onEmailChanged(text: String) {
        reduce {
            state.copy(
                email = text,
                emailErrorMessage = null
            )
        }
    }

    private suspend fun IntentBody.onPasswordChanged(text: String) {
        reduce {
            state.copy(
                password = text,
                passwordErrorMessage = null
            )
        }
    }

    private suspend fun IntentBody.onConfirmedPasswordChanged(text: String) {
        reduce {
            state.copy(
                confirmedPassword = text,
                confirmedPasswordErrorMessage = null
            )
        }
    }

    private suspend fun IntentBody.onPasswordVisibilityToggleClick() {
        reduce { state.copy(isPasswordVisible = !state.isPasswordVisible) }
    }

    private suspend fun IntentBody.onConfirmedPasswordVisibilityToggleClick() {
        reduce { state.copy(isConfirmedPasswordVisible = !state.isConfirmedPasswordVisible) }
    }

    private suspend fun onGoBackClick() {
        when {
            recoveryRouter.currentRoute == RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH &&
            recoveryRouter.previousRoute != null ->
                recoveryRouter.navigateBack()
            recoveryRouter.currentRoute == RECOVERY_SUCCESS_SCREEN_ROUTE_PATH &&
            recoveryRouter.previousRoute != null ->
                recoveryRouter.navigateBack()
            recoveryRouter.previousRoute == null ->
                router.navigateBack()
            else ->
                recoveryRouter.navigateBack()
        }
    }

    private suspend fun IntentBody.onResetPasswordButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(recoveringState = State.Recovering.InProgress) }

                resetPassword(email = state.email)

                reduce { state.copy(recoveringState = State.Recovering.None) }

                recoveryRouter.navigate(RECOVERY_CHECK_EMAIL_SCREEN_ROUTE_PATH)
            },
            onError = { throwable ->
                reduce { state.copy(recoveringState = State.Recovering.Error) }

                when (throwable) {
                    is InvalidRecoveryFieldsValuesException -> {
                        reduceError(throwable)
                    }

                    else -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))
                    }
                }

                delay(RECOVERING_STATE_DELAY_MILLIS)

                reduce { state.copy(recoveringState = State.Recovering.None) }
            }
        )
    }

    private suspend fun onCheckEmailOkButtonClick() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(SIGN_IN_SCREEN_ROUTE_PATH),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private suspend fun IntentBody.onConfirmNewPasswordButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(recoveringState = State.Recovering.InProgress) }

                resetPassword.changePassword(
                    password = state.password,
                    confirmedPassword = state.confirmedPassword,
                )

                reduce { state.copy(recoveringState = State.Recovering.Success) }

                delay(RECOVERING_STATE_DELAY_MILLIS)

                reduce { state.copy(recoveringState = State.Recovering.None) }

                recoveryRouter.navigate(
                    routePath = RECOVERY_SUCCESS_SCREEN_ROUTE_PATH,
                    options = listOf(
                        RoutingOption.PopUpTo(
                            routePath = RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH,
                            inclusive = true,
                        )
                    )
                )
            },
            onError = { throwable ->
                reduce { state.copy(recoveringState = State.Recovering.Error) }

                when (throwable) {
                    is InvalidRecoveryFieldsValuesException -> {
                        reduceError(throwable)
                    }

                    else -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))
                    }
                }

                delay(RECOVERING_STATE_DELAY_MILLIS)

                reduce { state.copy(recoveringState = State.Recovering.None) }
            }
        )
    }

    private suspend fun onFinishButtonClick() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(SIGN_IN_SCREEN_ROUTE_PATH),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private suspend fun IntentBody.reduceError(
        throwable: InvalidRecoveryFieldsValuesException
    ) {
        reduce {
            state.copy(
                emailErrorMessage = throwable.emailError.toStringResource(),
                passwordErrorMessage = throwable.passwordError.toStringResource(),
                confirmedPasswordErrorMessage = throwable.confirmedPasswordError.toStringResource(),
            )
        }
    }

    private fun InvalidRecoveryFieldsValuesException.Email?.toStringResource() = when (this) {
        InvalidRecoveryFieldsValuesException.Email.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }

    private fun InvalidRecoveryFieldsValuesException.Password?.toStringResource() = when (this) {
        InvalidRecoveryFieldsValuesException.Password.MIN_LENGTH -> strRes(R.string.error_password_to_short)
        null -> null
    }

    private fun InvalidRecoveryFieldsValuesException.ConfirmedPassword?.toStringResource() = when (this) {
        InvalidRecoveryFieldsValuesException.ConfirmedPassword.MISMATCH -> strRes(R.string.error_password_mismatch)
        null -> null
    }

    companion object {
        private const val RECOVERING_STATE_DELAY_MILLIS = 1000L
    }
}
