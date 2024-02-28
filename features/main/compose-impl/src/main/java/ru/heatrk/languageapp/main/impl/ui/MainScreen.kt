package ru.heatrk.languageapp.main.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.main.impl.di.MainComponent

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(
        factory = MainComponent.mainViewModelFactory
    ),
) {
    MainScreen()
}

@Composable
private fun MainScreen() {
    // TODO
}

@Composable
@Preview(showBackground = true)
private fun MainScreenPreview() {
    AppTheme {
        MainScreen()
    }
}
