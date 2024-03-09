package ru.heatrk.languageapp.onboarding.impl.ui.screens.onboarding

import ru.heatrk.languageapp.common.utils.StringResource
import ru.heatrk.languageapp.common.utils.VectorResource

object OnboardingContract {
    sealed interface State {
        data object Loading : State

        data class Loaded(
            val image: VectorResource,
            val title: StringResource,
            val description: StringResource,
            val progress: Int,
            val total: Int,
        ): State
    }

    sealed interface Intent {
        data object Skip : Intent
        data object Next : Intent
    }
}
