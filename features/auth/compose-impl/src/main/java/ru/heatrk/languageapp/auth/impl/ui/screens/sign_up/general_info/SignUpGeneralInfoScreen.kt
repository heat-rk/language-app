package ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.general_info

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpButtonsControllerEffect
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpTitle
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.text_field.AppTextField

@Composable
fun SignUpGeneralInfoScreen(viewModel: SignUpViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    SignUpButtonsControllerEffect(
        text = stringResource(R.string.button_continue),
        isLoginButtonVisible = true,
        onClick = { viewModel.processIntent(SignUpScreenContract.Intent.OnContinueButtonClick) }
    )

    SignUpGeneralInfoScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun SignUpGeneralInfoScreen(
    state: SignUpScreenContract.State,
    onIntent: (SignUpScreenContract.Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        SignUpTitle(titleRes = R.string.signup_create_account)

        Spacer(modifier = Modifier.height(32.dp))

        SignUpGeneralInfoFirstNameField(
            state = state,
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.height(24.dp))

        SignUpGeneralInfoLastNameField(
            state = state,
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.height(24.dp))

        SignUpGeneralInfoEmailField(
            state = state,
            onIntent = onIntent
        )
    }
}

@Composable
private fun SignUpGeneralInfoFirstNameField(
    state: SignUpScreenContract.State,
    onIntent: (SignUpScreenContract.Intent) -> Unit
) {
    AppTextField(
        value = state.firstName,
        isEnabled = state.registrationState == SignUpScreenContract.State.Registration.None,
        placeholder = stringResource(R.string.your_first_name),
        label = stringResource(R.string.first_name),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Words,
        ),
        errorMessage = state.firstNameErrorMessage?.extract(),
        onValueChange = { onIntent(SignUpScreenContract.Intent.OnFirstNameChanged(it)) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
private fun SignUpGeneralInfoLastNameField(
    state: SignUpScreenContract.State,
    onIntent: (SignUpScreenContract.Intent) -> Unit
) {
    AppTextField(
        value = state.lastName,
        isEnabled = state.registrationState == SignUpScreenContract.State.Registration.None,
        placeholder = stringResource(R.string.your_last_name),
        label = stringResource(R.string.last_name),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Words,
        ),
        errorMessage = state.lastNameErrorMessage?.extract(),
        onValueChange = { onIntent(SignUpScreenContract.Intent.OnLastNameChanged(it)) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
private fun SignUpGeneralInfoEmailField(
    state: SignUpScreenContract.State,
    onIntent: (SignUpScreenContract.Intent) -> Unit
) {
    AppTextField(
        value = state.email,
        isEnabled = state.registrationState == SignUpScreenContract.State.Registration.None,
        placeholder = stringResource(R.string.email),
        label = stringResource(R.string.email_address),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onIntent(SignUpScreenContract.Intent.OnContinueButtonClick)
            }
        ),
        errorMessage = state.emailErrorMessage?.extract(),
        onValueChange = { onIntent(SignUpScreenContract.Intent.OnEmailChanged(it)) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
private fun SignUpGeneralInfoScreenPreview() {
    AppRootContainer {
        SignUpGeneralInfoScreen(
            state = SignUpScreenContract.State(),
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SignUpGeneralInfoScreenPreviewLight() {
    SignUpGeneralInfoScreenPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SignUpGeneralInfoScreenPreviewDark() {
    SignUpGeneralInfoScreenPreview()
}
