package ru.heatrk.languageapp.main.impl.ui.screens.main

import ru.heatrk.languageapp.common.utils.PainterResource

object MainScreenContract {
    data class State(
        val profileState: Profile = Profile.Loading
    ) {
        sealed interface Profile {
            data object Loading : Profile

            data object Error : Profile

            data class Ok(
                val firstName: String,
                val avatar: PainterResource
            ) : Profile
        }
    }
}
