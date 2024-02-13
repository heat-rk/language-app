package ru.heatrk.languageapp.onboarding.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.onboarding.impl.di.OnboardingComponent

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = viewModel(
        factory = OnboardingComponent.onboardingViewModelFactory
    ),
) {
    // TODO()
}

@Composable
@Preview
private fun OnboardingScreenPreview() {
    AppTheme {
        OnboardingScreen()
    }
}
