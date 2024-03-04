package ru.heatrk.languageapp.auth.impl.ui.sign_up

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.State
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.Intent

class SignUpViewModel : ViewModel(), ContainerHost<State, Unit> {
    override val container = container<State, Unit>(
        initialState = State()
    )

    fun processIntent(intent: Intent) = intent {

    }
}
