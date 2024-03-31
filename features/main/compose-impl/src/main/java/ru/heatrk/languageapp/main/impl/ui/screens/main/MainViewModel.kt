package ru.heatrk.languageapp.main.impl.ui.screens.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.joinAll
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.common.utils.StringResource
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.profiles.api.domain.Profile
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.main.impl.R
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.Intent
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.SideEffect
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State
import ru.heatrk.languageapp.profile.api.domain.SettingsRepository
import ru.heatrk.languageapp.profile.api.ui.navigation.SELECT_LANGUAGE_SCREEN_ROUTE
import ru.heatrk.languageapp.profile.api.ui.navigation.SelectLanguageScreenArguments
import ru.heatrk.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class MainViewModel(
    private val profilesRepository: ProfilesRepository,
    private val settingsRepository: SettingsRepository,
    private val router: Router,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State()
    )

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
        loadUserData()
        loadLeaderboard()
    }

    private fun checkSavedLanguage() = intent {
        viewModelScope.launchSafe(
            block = {
                if (!settingsRepository.isLanguageAlreadySelected()) {
                    router.navigate(
                        routePath = SELECT_LANGUAGE_SCREEN_ROUTE,
                        arguments = mapOf(SelectLanguageScreenArguments.CAN_GO_BACK to false)
                    )
                }
            },
            onError = { /* ignored */ }
        ).join()
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
        ).join()
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

    private suspend fun IntentBody.onPulledToRefresh() {
        reduce { state.copy(isRefreshing = true) }

        joinAll(loadUserData(), loadLeaderboard())

        reduce { state.copy(isRefreshing = false) }
    }

    private fun toLeaderboardItem(profile: Profile) = State.Leaderboard.Item(
        id = profile.id,
        avatar = profile.avatarUrl?.let { url -> painterRes(Uri.parse(url)) },
        fullName = profile.fullName(),
        totalScore = profile.totalScore,
    )

    private fun Profile.fullName(): StringResource {
        val firstName = firstName
        val lastName = lastName

        return when {
            firstName == null && lastName == null ->
                strRes(R.string.main_leader_name_unknown)
            firstName == null ->
                strRes(lastName)
            lastName == null ->
                strRes(firstName)
            else ->
                strRes(DesignR.string.full_name_formatted, firstName, lastName)
        }
    }

    companion object {
        const val LEADERBOARD_ITEMS_COUNT = 3
    }
}
