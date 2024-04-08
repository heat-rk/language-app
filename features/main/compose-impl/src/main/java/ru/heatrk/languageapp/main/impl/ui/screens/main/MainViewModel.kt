package ru.heatrk.languageapp.main.impl.ui.screens.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.design.R
import ru.heatrk.languageapp.core.design.utils.formatFullName
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.profiles.api.domain.Profile
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.exercises.guess_animal.api.ui.navigation.GUESS_ANIMAL_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.exercises.word_practice.api.ui.navigation.WORD_PRACTICE_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.Intent
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.SideEffect
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State
import ru.heatrk.languageapp.profile.api.domain.SettingsRepository
import ru.heatrk.languageapp.profile.api.ui.navigation.PROFILE_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.profile.api.ui.navigation.SELECT_LANGUAGE_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.profile.api.ui.navigation.SelectLanguageScreenArguments

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class MainViewModel(
    private val profilesRepository: ProfilesRepository,
    private val settingsRepository: SettingsRepository,
    private val router: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

    private var observeCurrentProfileJob: Job? = null

    init {
        loadData()
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
            Intent.OnPulledToRefresh ->
                onPulledToRefresh()
        }
    }

    private fun loadData() {
        checkSavedLanguage()
        observeUserData(reload = false)
        loadLeaderboard()
    }

    private fun checkSavedLanguage() = intent {
        viewModelScope.launchSafe(
            block = {
                if (!settingsRepository.isLanguageAlreadySelected()) {
                    router.navigate(
                        routePath = SELECT_LANGUAGE_SCREEN_ROUTE_PATH,
                        arguments = mapOf(SelectLanguageScreenArguments.CAN_GO_BACK to false)
                    )
                }
            },
            onError = { /* ignored */ }
        ).join()
    }

    private fun observeUserData(reload: Boolean) = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(profileState = State.Profile.Loading) }

                observeCurrentProfileJob?.cancel()
                observeCurrentProfileJob = null

                observeCurrentProfileJob = profilesRepository.observeCurrentProfile(
                    reload = reload
                )
                    .onEach { profile ->
                        reduce {
                            state.copy(
                                profileState = State.Profile.Loaded(
                                    firstName = profile.firstName,
                                    avatar = profile.avatarUrl?.let { url ->
                                        painterRes(Uri.parse(url))
                                    },
                                )
                            )
                        }
                    }
                    .launchIn(this)
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(R.string.error_smth_went_wrong)))

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

    private fun loadLeaderboard() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(leaderboard = State.Leaderboard.Loading) }

                val leaderboard =
                    profilesRepository.getLeaderboard(LEADERBOARD_ITEMS_COUNT.toLong())

                reduce {
                    state.copy(
                        leaderboard = State.Leaderboard.Loaded(
                            items = leaderboard.map(::toLeaderboardItem)
                                .toImmutableList()
                        )
                    )
                }
            },
            onError = {
                reduce {
                    state.copy(
                        leaderboard = State.Leaderboard.Loaded(
                            items = persistentListOf()
                        )
                    )
                }
            }
        ).join()
    }

    private suspend fun onProfileClick() {
        router.navigate(routePath = PROFILE_GRAPH_ROUTE_PATH)
    }

    private suspend fun onGuessAnimalButtonClick() {
        router.navigate(routePath = GUESS_ANIMAL_SCREEN_ROUTE_PATH)
    }

    private suspend fun onWordPracticeButtonClick() {
        router.navigate(routePath = WORD_PRACTICE_SCREEN_ROUTE_PATH)
    }

    private fun onAuditionButtonClick() {
        // TODO
    }

    private fun onGameButtonClick() {
        // TODO
    }

    private suspend fun IntentBody.onPulledToRefresh() {
        reduce { state.copy(isRefreshing = true) }

        joinAll(observeUserData(reload = true), loadLeaderboard())

        reduce { state.copy(isRefreshing = false) }
    }

    private fun toLeaderboardItem(profile: Profile) = State.Leaderboard.Item(
        id = profile.id,
        avatar = profile.avatarUrl?.let { url -> painterRes(Uri.parse(url)) },
        fullName = formatFullName(
            firstName = profile.firstName,
            lastName = profile.lastName
        ),
        totalScore = profile.totalScore,
    )

    companion object {
        const val LEADERBOARD_ITEMS_COUNT = 3
    }
}
