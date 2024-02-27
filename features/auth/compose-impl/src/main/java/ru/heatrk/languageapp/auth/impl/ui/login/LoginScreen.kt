package ru.heatrk.languageapp.auth.impl.ui.login

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.login.LoginScreenContract.State
import ru.heatrk.languageapp.core.design.composables.AppBarState
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.AppButton
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

    LoginScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun LoginScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffold(
        appBarState = AppBarState.Shown(
            title = stringResource(R.string.login),
            titleGravity = AppBarTitleGravity.CENTER,
        ),
        contentPadding = PaddingValues(24.dp)
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

            EmailPasswordBlock(
                email = state.email,
                password = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onEmailValueChanged = { onIntent(Intent.OnEmailTextChanged(it)) },
                onPasswordValueChanged = { onIntent(Intent.OnPasswordTextChanged(it)) },
                onPasswordVisibilityToggleClick = { onIntent(Intent.OnPasswordVisibilityToggleClick) },
                onForgotPasswordButtonClick = { onIntent(Intent.OnForgotPasswordButtonClick) },
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginButtonsBlock(
                onLoginClick = { onIntent(Intent.OnLoginButtonClick) },
                onSignUpClick = { onIntent(Intent.OnSignUpButtonClick) },
                onGoogleSignInClick = { onIntent(Intent.OnGoogleSignInButtonClick) }
            )
        }
    }
}

@Composable
private fun LoginLinkedText(
    onClick: () -> Unit,
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
        modifier = modifier
    )
}

@Composable
private fun EmailPasswordBlock(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onEmailValueChanged: (String) -> Unit,
    onPasswordValueChanged: (String) -> Unit,
    onPasswordVisibilityToggleClick: () -> Unit,
    onForgotPasswordButtonClick: () -> Unit,
) {
    AppTextField(
        value = email,
        placeholder = stringResource(R.string.login_email_hint),
        label = stringResource(R.string.login_email),
        onValueChange = onEmailValueChanged,
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    AppPasswordTextField(
        value = password,
        isPasswordVisible = isPasswordVisible,
        label = stringResource(R.string.login_password),
        onValueChange = onPasswordValueChanged,
        onPasswordVisibilityToggleClick = onPasswordVisibilityToggleClick,
        modifier = Modifier
            .fillMaxWidth()
    )

    AppTextButton(
        text = stringResource(R.string.login_forgot_password),
        textColor = AppTheme.colors.error,
        contentPadding = PaddingValues(0.dp),
        onClick = onForgotPasswordButtonClick,
        modifier = Modifier
            .wrapContentWidth()
    )
}

@Composable
private fun ColumnScope.LoginButtonsBlock(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
) {
    AppButton(
        text = stringResource(R.string.login),
        onClick = onLoginClick,
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    LoginLinkedText(
        onClick = onSignUpClick,
        units = listOf(
            AppLinkedTextUnit(
                text = stringResource(R.string.login_not_you_member_before_annotation),
            ),
            AppLinkedTextUnit(
                text = stringResource(R.string.login_not_you_member_annotation),
                linkTag = "Signup"
            )
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )

    Spacer(modifier = Modifier.height(12.dp))

    LoginLinkedText(
        onClick = onGoogleSignInClick,
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
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
@Preview(showBackground = true)
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            state = State(),
            onIntent = {}
        )
    }
}
