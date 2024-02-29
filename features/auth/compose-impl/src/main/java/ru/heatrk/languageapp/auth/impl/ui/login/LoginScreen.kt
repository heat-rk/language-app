package ru.heatrk.languageapp.auth.impl.ui.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.auth.api.ui.navigation.LOGIN_SCREEN_TEST_TAG
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.State
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.SideEffect
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppBarState
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.AppButtonState
import ru.heatrk.languageapp.core.design.composables.AppLinkedText
import ru.heatrk.languageapp.core.design.composables.AppLinkedTextUnit
import ru.heatrk.languageapp.core.design.composables.AppPasswordTextField
import ru.heatrk.languageapp.core.design.composables.AppScaffold
import ru.heatrk.languageapp.core.design.composables.AppTextButton
import ru.heatrk.languageapp.core.design.composables.AppTextField
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(
        factory = AuthComponent.loginViewModelFactory
    ),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow
    val snackbarHostState = remember { SnackbarHostState() }

    LoginScreenSideEffects(
        sideEffects = sideEffects,
        snackbarHostState = snackbarHostState
    )

    LoginScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun LoginScreen(
    state: State,
    snackbarHostState: SnackbarHostState,
    onIntent: (Intent) -> Unit,
) {
    AppScaffold(
        snackbarHostState = snackbarHostState,
        appBarState = AppBarState.Shown(
            title = stringResource(R.string.login),
            titleGravity = AppBarTitleGravity.CENTER,
        ),
        contentPadding = PaddingValues(24.dp),
        modifier = Modifier
            .testTag(LOGIN_SCREEN_TEST_TAG)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.login_learn_at_home_logo),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.login_logo_title),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                style = AppTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginEmailPasswordBlock(
                email = state.email,
                password = state.password,
                emailErrorMessage = state.emailErrorMessage?.extract(),
                passwordErrorMessage = state.passwordErrorMessage?.extract(),
                isPasswordVisible = state.isPasswordVisible,
                onIntent = onIntent,
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginButtonsBlock(
                loginButtonState = state.authorizingState.toButtonState(),
                isLinkedTextEnabled = state.authorizingState == State.Authorizing.None,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun LoginEmailPasswordBlock(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    emailErrorMessage: String? = null,
    passwordErrorMessage: String? = null,
    onIntent: (Intent) -> Unit,
) {
    AppTextField(
        value = email,
        placeholder = stringResource(R.string.login_email_hint),
        label = stringResource(R.string.login_email),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        errorMessage = emailErrorMessage,
        onValueChange = { onIntent(Intent.OnEmailTextChanged(it)) },
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    AppPasswordTextField(
        value = password,
        isPasswordVisible = isPasswordVisible,
        label = stringResource(R.string.login_password),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onIntent(Intent.OnLoginButtonClick) }
        ),
        errorMessage = passwordErrorMessage,
        onValueChange = { onIntent(Intent.OnPasswordTextChanged(it)) },
        onPasswordVisibilityToggleClick = { onIntent(Intent.OnPasswordVisibilityToggleClick) },
        modifier = Modifier
            .fillMaxWidth()
    )

    AppTextButton(
        text = stringResource(R.string.login_forgot_password),
        textColor = AppTheme.colors.error,
        contentPadding = PaddingValues(0.dp),
        onClick = { onIntent(Intent.OnForgotPasswordButtonClick) },
        modifier = Modifier
            .wrapContentWidth()
    )
}

@Composable
private fun ColumnScope.LoginButtonsBlock(
    loginButtonState: AppButtonState,
    isLinkedTextEnabled: Boolean,
    onIntent: (Intent) -> Unit,
) {
    AppButton(
        text = stringResource(R.string.login),
        buttonState = loginButtonState,
        onClick = { onIntent(Intent.OnLoginButtonClick) },
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    LoginLinkedText(
        onClick = { onIntent(Intent.OnSignUpButtonClick) },
        units = listOf(
            AppLinkedTextUnit(
                text = stringResource(R.string.login_not_you_member_before_annotation),
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.login_not_you_member_annotation),
                linkTag = "Signup"
            )
        ),
        isEnabled = isLinkedTextEnabled,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )

    Spacer(modifier = Modifier.height(12.dp))

    LoginLinkedText(
        onClick = { onIntent(Intent.OnGoogleSignInButtonClick) },
        units = listOf(
            AppLinkedTextUnit(
                text = stringResource(R.string.login_use_google_before_annotation),
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.login_use_google_annotation),
                linkTag = "Signup"
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.login_use_google_after_annotation),
            )
        ),
        isEnabled = isLinkedTextEnabled,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun LoginLinkedText(
    onClick: () -> Unit,
    isEnabled: Boolean,
    units: List<AppLinkedTextUnit>,
    modifier: Modifier = Modifier,
) {
    AppLinkedText(
        onClick = { onClick() },
        units = units,
        textSpanStyle = SpanStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textBody,
        ),
        linkSpanStyle = SpanStyle(
            color = AppTheme.colors.secondary,
        ),
        isEnabled = isEnabled,
        modifier = modifier
    )
}

@Composable
private fun LoginScreenSideEffects(
    sideEffects: Flow<SideEffect>,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(sideEffects, context) {
        sideEffects
            .onEach { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Message -> {
                        handleMessageSideEffect(
                            sideEffect = sideEffect,
                            snackbarHostState = snackbarHostState,
                            context = context,
                        )
                    }

                    is SideEffect.CloseKeyboard -> {
                        keyboardController?.hide()
                    }
                }
            }
            .launchIn(this)
    }
}

private suspend fun handleMessageSideEffect(
    sideEffect: SideEffect.Message,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    val message = sideEffect.text.extract(context)
        ?: return

    snackbarHostState.showSnackbar(message)
}

private fun State.Authorizing.toButtonState() = when (this) {
    State.Authorizing.None -> AppButtonState.Idle
    State.Authorizing.InProgress -> AppButtonState.Loading
    State.Authorizing.Success -> AppButtonState.Success
    State.Authorizing.Error -> AppButtonState.Error
}

@Composable
@Preview(showBackground = true)
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            state = State(),
            snackbarHostState = SnackbarHostState(),
            onIntent = {}
        )
    }
}
