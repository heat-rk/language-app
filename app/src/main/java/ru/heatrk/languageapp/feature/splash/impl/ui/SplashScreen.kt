package ru.heatrk.languageapp.feature.splash.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.feature.splash.impl.di.SplashComponent
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = viewModel(
        factory = SplashComponent.splashViewModelFactory
    )
) {
    // TODO
}

@Composable
@Preview
private fun SplashScreenPreview() {
    AppTheme {
        SplashScreen()
    }
}
