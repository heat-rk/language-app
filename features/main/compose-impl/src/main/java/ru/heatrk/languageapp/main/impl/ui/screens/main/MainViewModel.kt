package ru.heatrk.languageapp.main.impl.ui.screens.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.Intent
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.SideEffect
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class MainViewModel(
    private val profilesRepository: ProfilesRepository
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    init {
        loadUserData()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            Intent.OnProfileClick ->
                onProfileClick()
            Intent.OnAuditionButtonClick ->
                onAuditionButtonClick()
            Intent.OnGameButtonClick ->
                onGameButtonClick()
            Intent.OnGuessAnimalButtonClick ->
                onGuessAnimalButtonClick()
            Intent.OnWordPracticeButtonClick ->
                onWordPracticeButtonClick()
        }
    }

    private fun loadUserData() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(profileState = State.Profile.Loading) }

                val user = profilesRepository.getCurrentProfile()

                reduce {
                    state.copy(
                        profileState = State.Profile.Loaded(
                            firstName = user.firstName,
                            avatar = user.avatarUrl?.let { url ->
                                painterRes(Uri.parse(url))
                            },
                        )
                    )
                }
            },
            onError = {
                reduce {
                    state.copy(
                        profileState = State.Profile.Loaded(
                            firstName = null,
                            avatar = null,
                        )
                    )
                }
            }
        )
    }

    private fun onProfileClick() {
        // TODO
    }

    private fun onGuessAnimalButtonClick() {
        // TODO
    }

    private fun onWordPracticeButtonClick() {
        // TODO
    }

    private fun onAuditionButtonClick() {
        // TODO
    }

    private fun onGameButtonClick() {
        // TODO
    }
}
