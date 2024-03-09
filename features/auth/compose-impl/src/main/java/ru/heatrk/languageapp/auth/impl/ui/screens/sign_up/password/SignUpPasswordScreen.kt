package ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.password

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.ui.composables.choose_password.ChoosePassword
import ru.heatrk.languageapp.auth.impl.ui.composables.choose_password.ChoosePasswordContract
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpButtonsControllerEffect
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.heatrk.languageapp.core.design.composables.AppRootContainer

@Composable
fun SignUpPasswordScreen(viewModel: SignUpViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    SignUpPasswordScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun SignUpPasswordScreen(
    state: SignUpScreenContract.State,
    onIntent: (SignUpScreenContract.Intent) -> Unit,
) {
    SignUpButtonsControllerEffect(
        text = stringResource(R.string.signup),
        isLoginButtonVisible = true,
        onClick = { onIntent(SignUpScreenContract.Intent.OnSignUpButtonClick) }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        ChoosePassword(
            state = ChoosePasswordContract.State(
                password = state.password,
                confirmedPassword = state.confirmedPassword,
                passwordErrorMessage = state.passwordErrorMessage,
                confirmedPasswordErrorMessage = state.confirmedPasswordErrorMessage,
                isPasswordVisible = state.isPasswordVisible,
                isConfirmedPasswordVisible = state.isConfirmedPasswordVisible,
                isEnabled = state.registrationState == SignUpScreenContract.State.Registration.None,
            ),
            onIntent = { intent ->
                when (intent) {
                    is ChoosePasswordContract.Intent.OnPasswordChanged ->
                        onIntent(SignUpScreenContract.Intent.OnPasswordChanged(intent.text))
                    is ChoosePasswordContract.Intent.OnConfirmedPasswordChanged ->
                        onIntent(SignUpScreenContract.Intent.OnConfirmedPasswordChanged(intent.text))
                    ChoosePasswordContract.Intent.OnPasswordVisibilityToggleClick ->
                        onIntent(SignUpScreenContract.Intent.OnPasswordVisibilityToggleClick)
                    ChoosePasswordContract.Intent.OnConfirmedPasswordVisibilityToggleClick ->
                        onIntent(SignUpScreenContract.Intent.OnConfirmedPasswordVisibilityToggleClick)
                    ChoosePasswordContract.Intent.OnKeyboardDoneAction ->
                        onIntent(SignUpScreenContract.Intent.OnSignUpButtonClick)
                }
            }
        )
    }
}

@Composable
private fun SignUpPasswordScreenPreview() {
    AppRootContainer {
        SignUpPasswordScreen(
            state = SignUpScreenContract.State(),
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SignUpPasswordScreenPreviewLight() {
    SignUpPasswordScreenPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SignUpPasswordScreenPreviewDark() {
    SignUpPasswordScreenPreview()
}
