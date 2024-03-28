package ru.heatrk.languageapp.main.impl.ui.screens.main

import ru.heatrk.languageapp.common.utils.PainterResource
import ru.heatrk.languageapp.common.utils.StringResource

object MainScreenContract {
    data class State(
        val profileState: Profile = Profile.Loading
    ) {
        sealed interface Profile {
            data object Loading : Profile

            data class Loaded(
                val firstName: String?,
                val avatar: PainterResource?
            ) : Profile
        }
    }

    sealed interface Intent {
        data object OnProfileClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
    }
}
