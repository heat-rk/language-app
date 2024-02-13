package ru.heatrk.languageapp.onboarding.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            delay(SPLASH_DELAY)

        }
    }

    companion object {
        private const val SPLASH_DELAY = 1000L
    }
}
