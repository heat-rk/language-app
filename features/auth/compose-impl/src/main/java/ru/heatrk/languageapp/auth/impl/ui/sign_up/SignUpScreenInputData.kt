package ru.heatrk.languageapp.auth.impl.ui.sign_up

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.ui.choose_password.ChoosePassword
import ru.heatrk.languageapp.auth.impl.ui.choose_password.ChoosePasswordContract
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.State
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppLinkedText
import ru.heatrk.languageapp.core.design.composables.AppLinkedTextUnit
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.animation.RightToLeftAnimatedContent
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.AppButtonState
import ru.heatrk.languageapp.core.design.composables.text_field.AppTextField
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun SignUpScreenInputData(
    state: State.InputData,
    onIntent: (Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 24.dp,
                vertical = 40.dp,
            )
    ) {
        RightToLeftAnimatedContent(
            targetState = state.inputDataState,
            label = "InputDataStateAnimation"
        ) { inputDataState ->
            when (inputDataState) {
                State.InputData.State.GENERAL -> {
                    SignUpGeneralInputData(
                        state = state,
                        onIntent = onIntent
                    )
                }
                State.InputData.State.PASSWORD -> {
                    SignUpPasswordInputData(
                        state = state,
                        onIntent = onIntent
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(34.dp))

        SignUpButtons(
            state = state,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun SignUpGeneralInputData(
    state: State.InputData,
    onIntent: (Intent) -> Unit,
) {
    Column {
        SignUpTitle(
            titleRes = R.string.signup_create_account
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppTextField(
            value = state.firstName,
            isEnabled = state.registrationState == State.InputData.Registration.None,
            placeholder = stringResource(R.string.your_first_name),
            label = stringResource(R.string.first_name),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            errorMessage = state.firstNameErrorMessage?.extract(),
            onValueChange = { onIntent(Intent.OnFirstNameChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = state.lastName,
            isEnabled = state.registrationState == State.InputData.Registration.None,
            placeholder = stringResource(R.string.your_last_name),
            label = stringResource(R.string.last_name),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            errorMessage = state.lastNameErrorMessage?.extract(),
            onValueChange = { onIntent(Intent.OnLastNameChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = state.email,
            isEnabled = state.registrationState == State.InputData.Registration.None,
            placeholder = stringResource(R.string.email),
            label = stringResource(R.string.email_address),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onIntent(Intent.OnContinueButtonClick)
                }
            ),
            errorMessage = state.emailErrorMessage?.extract(),
            onValueChange = { onIntent(Intent.OnEmailChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SignUpPasswordInputData(
    state: State.InputData,
    onIntent: (Intent) -> Unit,
) {
    ChoosePassword(
        state = ChoosePasswordContract.State(
            password = state.password,
            confirmedPassword = state.confirmedPassword,
            passwordErrorMessage = state.passwordErrorMessage,
            confirmedPasswordErrorMessage = state.confirmedPasswordErrorMessage,
            isPasswordVisible = state.isPasswordVisible,
            isConfirmedPasswordVisible = state.isConfirmedPasswordVisible,
            isEnabled = state.registrationState == State.InputData.Registration.None,
        ),
        onIntent = { intent ->
            when (intent) {
                is ChoosePasswordContract.Intent.OnPasswordChanged ->
                    onIntent(Intent.OnPasswordChanged(intent.text))
                is ChoosePasswordContract.Intent.OnConfirmedPasswordChanged ->
                    onIntent(Intent.OnConfirmedPasswordChanged(intent.text))
                ChoosePasswordContract.Intent.OnPasswordVisibilityToggleClick ->
                    onIntent(Intent.OnPasswordVisibilityToggleClick)
                ChoosePasswordContract.Intent.OnConfirmedPasswordVisibilityToggleClick ->
                    onIntent(Intent.OnConfirmedPasswordVisibilityToggleClick)
                ChoosePasswordContract.Intent.OnKeyboardDoneAction ->
                    onIntent(Intent.OnSignUpButtonClick)
            }
        }
    )
}

@Composable
private fun ColumnScope.SignUpButtons(
    state: State.InputData,
    onIntent: (Intent) -> Unit,
) {
    AppButton(
        text = when (state.inputDataState) {
            State.InputData.State.GENERAL -> {
                stringResource(R.string.button_continue)
            }
            State.InputData.State.PASSWORD -> {
                stringResource(R.string.signup)
            }
        },
        buttonState = state.registrationState.toButtonState(),
        onClick = {
            onIntent(
                when (state.inputDataState) {
                    State.InputData.State.GENERAL -> {
                        Intent.OnContinueButtonClick
                    }
                    State.InputData.State.PASSWORD -> {
                        Intent.OnSignUpButtonClick
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    AppLinkedText(
        onClick = { onIntent(Intent.OnSignInButtonClick) },
        units = listOf(
            AppLinkedTextUnit(
                text = stringResource(R.string.signup_already_you_member_before_annotation),
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.signup_already_you_member_after_annotation),
                linkTag = "sign_up",
            )
        ),
        textSpanStyle = SpanStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textBody,
        ),
        linkSpanStyle = SpanStyle(
            color = AppTheme.colors.secondary,
        ),
        isEnabled = state.registrationState == State.InputData.Registration.None,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun ColumnScope.SignUpTitle(
    @StringRes titleRes: Int
) {
    Text(
        text = stringResource(titleRes),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        style = AppTheme.typography.titleLarge,
        modifier = Modifier
            .padding(horizontal = 56.dp)
            .align(Alignment.CenterHorizontally)
    )
}

private fun State.InputData.Registration.toButtonState() = when (this) {
    State.InputData.Registration.None -> AppButtonState.Idle
    State.InputData.Registration.InProgress -> AppButtonState.Loading
    State.InputData.Registration.Success -> AppButtonState.Success
    State.InputData.Registration.Error -> AppButtonState.Error
}

class SignUpInputDataStatePreviewParameterProvider : PreviewParameterProvider<State.InputData.State> {
    override val values =
        State.InputData.State.entries.asSequence()
}

@Composable
private fun SignUpScreenInputDataPreview(
    inputDataState: State.InputData.State
) {
    AppRootContainer {
        SignUpScreenInputData(
            state = State.InputData(
                inputDataState = inputDataState
            ),
            onIntent = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun SignUpScreenInputDataPreviewLight(
    @PreviewParameter(SignUpInputDataStatePreviewParameterProvider::class)
    inputDataState: State.InputData.State
) {
    SignUpScreenInputDataPreview(inputDataState = inputDataState)
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun SignUpScreenInputDataPreviewDark(
    @PreviewParameter(SignUpInputDataStatePreviewParameterProvider::class)
    inputDataState: State.InputData.State
) {
    SignUpScreenInputDataPreview(inputDataState = inputDataState)
}
