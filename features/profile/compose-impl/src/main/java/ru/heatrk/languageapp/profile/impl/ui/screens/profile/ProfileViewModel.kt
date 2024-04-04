package ru.heatrk.languageapp.profile.impl.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.design.utils.formatFullName
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.profile.api.domain.ForcedTheme
import ru.heatrk.languageapp.profile.api.domain.SettingsRepository
import ru.heatrk.languageapp.profile.api.ui.navigation.SELECT_LANGUAGE_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.profile.api.ui.navigation.SelectLanguageScreenArguments
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileContract.Intent
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileContract.SideEffect
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileContract.State
import ru.heatrk.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

class ProfileViewModel(
    private val router: Router,
    private val profilesRepository: ProfilesRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Loading
    )

    init {
        loadData()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnChangeAvatarButtonClick ->
                onChangeAvatarButtonClick()
            Intent.OnChangeLanguageButtonClick ->
                onChangeLanguageButtonClick()
            Intent.OnLogoutButtonClick ->
                onLogoutButtonClick()
            is Intent.OnSwitchUiModeButtonClick ->
                onSwitchUiModeButtonClick(intent.toDarkTheme)
        }
    }

    private fun loadData() {
        loadProfileData()
    }

    private fun loadProfileData() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { State.Loading }

                val user = profilesRepository.getCurrentProfile()

                reduce {
                    State.Loaded(
                        fullName = formatFullName(
                            firstName = user.firstName,
                            lastName = user.lastName,
                        ),
                        avatar = user.avatarUrl?.let { url ->
                            painterRes(Uri.parse(url))
                        },
                    )
                }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
            }
        )
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private suspend fun onChangeAvatarButtonClick() {
        // TODO
    }

    private suspend fun onChangeLanguageButtonClick() {
        router.navigate(
            routePath = SELECT_LANGUAGE_SCREEN_ROUTE_PATH,
            arguments = mapOf(
                SelectLanguageScreenArguments.CAN_GO_BACK to true
            )
        )
    }

    private suspend fun onLogoutButtonClick() {
        // TODO
    }

    private suspend fun onSwitchUiModeButtonClick(toDarkTheme: Boolean) {
        settingsRepository.forceTheme(
            if (toDarkTheme) {
                ForcedTheme.DARK
            } else {
                ForcedTheme.LIGHT
            }
        )
    }
}
