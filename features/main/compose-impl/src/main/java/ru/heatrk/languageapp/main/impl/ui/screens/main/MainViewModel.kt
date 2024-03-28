package ru.heatrk.languageapp.main.impl.ui.screens.main

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State

class MainViewModel : ViewModel(), ContainerHost<State, Unit> {
    override val container = container<State, Unit>(
        initialState = State()
    )
}
