package ru.heatrk.languageapp.login.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.login.api.LOGIN_SCREEN_TEST_TAG
import ru.heatrk.languageapp.login.impl.di.LoginComponent

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(
        factory = LoginComponent.loginViewModelFactory
    ),
) {
    LoginScreen()
}

@Composable
private fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LOGIN_SCREEN_TEST_TAG)
    )
}

@Composable
@Preview
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}
