package ru.heatrk.languageapp.profile.impl.ui.screens.profile

import ru.heatrk.languageapp.common.utils.PainterResource
import ru.heatrk.languageapp.common.utils.StringResource

object ProfileContract {
    sealed interface State {
        data object Loading : State

        data class Loaded(
            val fullName: StringResource?,
            val avatar: PainterResource?
        ) : State
    }

    sealed interface Intent {
        data class OnSwitchUiModeButtonClick(val toDarkTheme: Boolean) : Intent
        data object OnChangeLanguageButtonClick : Intent
        data object OnChangeAvatarButtonClick : Intent
        data object OnLogoutButtonClick : Intent
        data object OnGoBackClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
    }
}
