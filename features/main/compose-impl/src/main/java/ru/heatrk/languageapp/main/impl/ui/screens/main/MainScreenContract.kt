package ru.heatrk.languageapp.main.impl.ui.screens.main

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.heatrk.languageapp.common.utils.PainterResource
import ru.heatrk.languageapp.common.utils.StringResource

object MainScreenContract {
    data class State(
        val profileState: Profile = Profile.Loading,
        val leaderboard: Leaderboard = Leaderboard.Loading,
    ) {
        sealed interface Profile {
            data object Loading : Profile

            data class Loaded(
                val firstName: String?,
                val avatar: PainterResource?
            ) : Profile
        }

        sealed interface Leaderboard {
            data object Loading : Leaderboard

            data class Loaded(
                val items: ImmutableList<Item> = persistentListOf(),
            ) : Leaderboard

            data class Item(
                val id: String,
                val fullName: StringResource?,
                val avatar: PainterResource?,
                val totalScore: Int,
            )
        }
    }

    sealed interface Intent {
        data object OnProfileClick : Intent
        data object OnGuessAnimalButtonClick : Intent
        data object OnWordPracticeButtonClick : Intent
        data object OnAuditionButtonClick : Intent
        data object OnGameButtonClick : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
    }
}
