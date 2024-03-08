package ru.heatrk.languageapp.auth.impl.ui.sign_up

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
import ru.heatrk.languageapp.auth.impl.domain.sign_up.InvalidSignUpFieldsValuesException
import ru.heatrk.languageapp.auth.impl.domain.sign_up.SignUpUseCase
import ru.heatrk.languageapp.auth.impl.ui.navigation.SIGN_IN_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.SideEffect
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.State
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.letIfInheritor
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOptions

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class SignUpViewModel(
    private val router: Router,
    private val signUp: SignUpUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.InputData()
    )

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnEmailChanged ->
                onEmailChanged(intent.text)
            is Intent.OnFirstNameChanged ->
                onFirstNameChanged(intent.text)
            is Intent.OnLastNameChanged ->
                onLastNameChanged(intent.text)
            is Intent.OnPasswordChanged ->
                onPasswordChanged(intent.text)
            is Intent.OnConfirmedPasswordChanged ->
                onConfirmedPasswordChanged(intent.text)
            Intent.OnContinueButtonClick ->
                onContinueButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnPasswordVisibilityToggleClick ->
                onPasswordVisibilityToggleClick()
            Intent.OnConfirmedPasswordVisibilityToggleClick ->
                onConfirmedPasswordVisibilityToggleClick()
            Intent.OnSignInButtonClick ->
                onSignInButtonClick()
            Intent.OnSignUpButtonClick ->
                onSignUpButtonClick()
            Intent.OnFinishButtonClick ->
                onFinishButtonClick()
        }
    }

    private suspend fun IntentBody.onFirstNameChanged(text: String) {
        reduceIfState<State.InputData> { state ->
            state.letIfInheritor<State, State.InputData> { state ->
                state.copy(
                    firstName = text,
                    firstNameErrorMessage = null,
                )
            }
        }
    }

    private suspend fun IntentBody.onLastNameChanged(text: String) {
        reduceIfState<State.InputData> { state ->
            state.copy(
                lastName = text,
                lastNameErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onEmailChanged(text: String) {
        reduceIfState<State.InputData> { state ->
            state.copy(
                email = text,
                emailErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onPasswordChanged(text: String) {
        reduceIfState<State.InputData> { state ->
            state.copy(
                password = text,
                passwordErrorMessage = null,
            )
        }
    }

    private suspend fun IntentBody.onConfirmedPasswordChanged(text: String) {
        reduceIfState<State.InputData> { state ->
            state.copy(
                confirmedPassword = text,
                confirmedPasswordErrorMessage = null,
            )
        }
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private suspend fun IntentBody.onPasswordVisibilityToggleClick() {
        reduceIfState<State.InputData> { state ->
            state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    private suspend fun IntentBody.onConfirmedPasswordVisibilityToggleClick() {
        reduceIfState<State.InputData> { state ->
            state.copy(
                isConfirmedPasswordVisible = !state.isConfirmedPasswordVisible
            )
        }
    }

    private suspend fun onSignInButtonClick() {
        navigateToSignIn()
    }

    private suspend fun IntentBody.onContinueButtonClick() {
        val state = state

        if (state !is State.InputData) {
            return
        }

        viewModelScope.launchSafe(
            block = {
                signUp.validate(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    email = state.email,
                    fieldsToValidate = listOf(
                        SignUpUseCase.Field.FIRST_NAME,
                        SignUpUseCase.Field.LAST_NAME,
                        SignUpUseCase.Field.EMAIL,
                    ),
                )

                reduceIfState<State.InputData> { state ->
                    state.copy(
                        inputDataState = State.InputData.State.PASSWORD
                    )
                }
            },
            onError = { throwable ->
                reduceIfState<State.InputData> { state ->
                    state.copy(registrationState = State.InputData.Registration.Error)
                }

                when (throwable) {
                    is InvalidSignUpFieldsValuesException -> {
                        reduceErrors(throwable)
                    }
                    else -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))
                    }
                }

                delay(REGISTRATION_STATE_DELAY_MILLIS)

                reduceIfState<State.InputData> { state ->
                    state.copy(registrationState = State.InputData.Registration.None)
                }
            }
        )
    }

    private suspend fun IntentBody.onSignUpButtonClick() {
        val state = state

        if (state !is State.InputData) {
            return
        }

        viewModelScope.launchSafe(
            block = {
                reduceIfState<State.InputData> { state ->
                    state.copy(registrationState = State.InputData.Registration.InProgress)
                }

                signUp(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    email = state.email,
                    password = state.password,
                    confirmedPassword = state.confirmedPassword,
                )

                reduceIfState<State.InputData> { state ->
                    state.copy(registrationState = State.InputData.Registration.Success)
                }

                delay(REGISTRATION_STATE_DELAY_MILLIS)

                reduceIfState<State.InputData> { State.EmailConfirmation }
            },
            onError = { throwable ->
                reduceIfState<State.InputData> { state ->
                    state.copy(registrationState = State.InputData.Registration.Error)
                }

                when (throwable) {
                    is InvalidSignUpFieldsValuesException -> {
                        reduceErrors(throwable)
                    }

                    is BadRequestRestException -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_check_your_creds)))
                    }

                    else -> {
                        postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))
                    }
                }

                delay(REGISTRATION_STATE_DELAY_MILLIS)

                reduceIfState<State.InputData> { state ->
                    state.copy(registrationState = State.InputData.Registration.None)
                }
            }
        )
    }

    private suspend fun onFinishButtonClick() {
        navigateToSignIn()
    }

    private suspend fun IntentBody.reduceErrors(
        throwable: InvalidSignUpFieldsValuesException
    ) {
        reduceIfState<State.InputData> { state ->
            state.copy(
                firstNameErrorMessage = throwable.firstNameError.toStringResource(),
                lastNameErrorMessage = throwable.lastNameError.toStringResource(),
                emailErrorMessage = throwable.emailError.toStringResource(),
                passwordErrorMessage = throwable.passwordError.toStringResource(),
                confirmedPasswordErrorMessage = throwable.confirmedPasswordError.toStringResource(),
            )
        }
    }

    private suspend inline fun <reified T: State> IntentBody.reduceIfState(
        crossinline reducer: (state: T) -> State
    ) {
        reduce {
            state.letIfInheritor<State, T> { state ->
                reducer(state)
            }
        }
    }

    private suspend fun navigateToSignIn() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = RoutingOptions(
                singleTop = true
            )
        )
    }

    private fun InvalidSignUpFieldsValuesException.Name?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.Name.EMPTY -> strRes(R.string.error_empty_field)
        null -> null
    }

    private fun InvalidSignUpFieldsValuesException.Email?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.Email.INVALID_FORMAT -> strRes(R.string.error_invalid_email)
        null -> null
    }

    private fun InvalidSignUpFieldsValuesException.Password?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.Password.MIN_LENGTH -> strRes(R.string.error_password_to_short)
        null -> null
    }

    private fun InvalidSignUpFieldsValuesException.ConfirmedPassword?.toStringResource() = when (this) {
        InvalidSignUpFieldsValuesException.ConfirmedPassword.MISMATCH -> strRes(R.string.error_password_mismatch)
        null -> null
    }

    companion object {
        private const val REGISTRATION_STATE_DELAY_MILLIS = 1000L
    }
}
