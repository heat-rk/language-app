package ru.heatrk.languageapp.profile.impl.ui.screens.profile

import ru.heatrk.languageapp.common.utils.PainterResource

object ProfileContract {
    sealed interface State {
        data object Loading : State

        data class Loaded(
            val fullName: String?,
            val avatar: PainterResource?
        ) : State
    }

    sealed interface Intent {
        data object OnSwitchUiModeButtonClick : Intent
        data object OnChangeLanguageButtonClick : Intent
        data object OnChangeAvatarButtonClick : Intent
        data object OnLogoutButtonClick : Intent
        data object OnGoBackClick : Intent
    }
}
