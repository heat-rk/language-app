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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.heatrk.languageapp.auth.api.ui.navigation.SIGN_UP_SCREEN_TEST_TAG
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.State
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.AppLinkedText
import ru.heatrk.languageapp.core.design.composables.AppLinkedTextUnit
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.animation.RightToLeftAnimatedContent
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.AppButtonState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.text_field.AppPasswordTextField
import ru.heatrk.languageapp.core.design.composables.text_field.AppTextField
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = viewModel(
        factory = AuthComponent.signUpViewModelFactory
    )
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    SignUpScreen(
        state = state,
        onIntent = viewModel::processIntent,
    )
}

@Composable
private fun SignUpScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    val appBarTitle = stringResource(R.string.signup)

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Shown(
            title = appBarTitle,
            titleGravity = AppBarTitleGravity.CENTER,
            onGoBackClick = { onIntent(Intent.OnGoBackClick) },
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(SIGN_UP_SCREEN_TEST_TAG)
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
                State.InputDataState.GENERAL -> {
                    SignUpGeneralInputData(
                        state = state,
                        onIntent = onIntent
                    )
                }
                State.InputDataState.PASSWORD -> {
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
    state: State,
    onIntent: (Intent) -> Unit,
) {
    Column {
        SignUpTitle(
            titleRes = R.string.signup_create_account
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppTextField(
            value = state.firstName,
            isEnabled = state.registrationState == State.Registration.None,
            placeholder = stringResource(R.string.your_first_name),
            label = stringResource(R.string.first_name),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            errorMessage = state.firstNameErrorMessage,
            onValueChange = { onIntent(Intent.OnFirstNameChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = state.lastName,
            isEnabled = state.registrationState == State.Registration.None,
            placeholder = stringResource(R.string.your_last_name),
            label = stringResource(R.string.last_name),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            errorMessage = state.lastNameErrorMessage,
            onValueChange = { onIntent(Intent.OnLastNameChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = state.email,
            isEnabled = state.registrationState == State.Registration.None,
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
            errorMessage = state.emailErrorMessage,
            onValueChange = { onIntent(Intent.OnEmailChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SignUpPasswordInputData(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    Column {
        SignUpTitle(
            titleRes = R.string.signup_choose_password
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppPasswordTextField(
            value = state.password,
            isPasswordVisible = state.isPasswordVisible,
            isEnabled = state.registrationState == State.Registration.None,
            label = stringResource(R.string.password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            errorMessage = state.passwordErrorMessage,
            onValueChange = { onIntent(Intent.OnPasswordChanged(it)) },
            onPasswordVisibilityToggleClick = { onIntent(Intent.OnPasswordVisibilityToggleClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppPasswordTextField(
            value = state.confirmedPassword,
            isPasswordVisible = state.isConfirmedPasswordVisible,
            isEnabled = state.registrationState == State.Registration.None,
            label = stringResource(R.string.confirm_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onIntent(Intent.OnSignUpButtonClick)
                }
            ),
            errorMessage = state.confirmedPasswordErrorMessage,
            onValueChange = { onIntent(Intent.OnConfirmedPasswordChanged(it)) },
            onPasswordVisibilityToggleClick = { onIntent(Intent.OnConfirmedPasswordVisibilityToggleClick) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ColumnScope.SignUpButtons(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppButton(
        text = when (state.inputDataState) {
            State.InputDataState.GENERAL -> {
                stringResource(R.string.button_continue)
            }
            State.InputDataState.PASSWORD -> {
                stringResource(R.string.signup)
            }
        },
        buttonState = state.registrationState.toButtonState(),
        onClick = {
            onIntent(
                when (state.inputDataState) {
                    State.InputDataState.GENERAL -> {
                        Intent.OnContinueButtonClick
                    }
                    State.InputDataState.PASSWORD -> {
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
        isEnabled = state.registrationState == State.Registration.None,
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

private fun State.Registration.toButtonState() = when (this) {
    State.Registration.None -> AppButtonState.Idle
    State.Registration.InProgress -> AppButtonState.Loading
    State.Registration.Success -> AppButtonState.Success
    State.Registration.Error -> AppButtonState.Error
}

class InputDataStatePreviewParameterProvider : PreviewParameterProvider<State.InputDataState> {
    override val values =
        State.InputDataState.entries.asSequence()
}

@Composable
private fun SignUpScreenPreview(
    inputDataState: State.InputDataState
) {
    AppRootContainer {
        SignUpScreen(
            state = State(
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
private fun SignUpScreenPreviewLight(
    @PreviewParameter(InputDataStatePreviewParameterProvider::class)
    inputDataState: State.InputDataState
) {
    SignUpScreenPreview(
        inputDataState = inputDataState
    )
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun SignUpScreenPreviewDark(
    @PreviewParameter(InputDataStatePreviewParameterProvider::class)
    inputDataState: State.InputDataState
) {
    SignUpScreenPreview(
        inputDataState = inputDataState
    )
}
