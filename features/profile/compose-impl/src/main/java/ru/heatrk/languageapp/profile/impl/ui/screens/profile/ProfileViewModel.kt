package ru.heatrk.languageapp.profile.impl.ui.screens.profile

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.profile.api.ui.navigation.SELECT_LANGUAGE_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.profile.api.ui.navigation.SelectLanguageScreenArguments
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileContract.State
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileContract.Intent

class ProfileViewModel(
    private val router: Router
) : ViewModel(), ContainerHost<State, Unit> {
    override val container = container<State, Unit>(
        initialState = State.Loading
    )

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
            Intent.OnSwitchUiModeButtonClick ->
                onSwitchUiModeButtonClick()
        }
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

    private suspend fun onSwitchUiModeButtonClick() {
        // TODO
    }
}
