package ru.heatrk.languageapp.feature.splash.impl.ui

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class SplashViewModel : ViewModel(), ContainerHost<Unit, Unit> {
    override val container = container<Unit, Unit>(
        initialState = Unit
    )
}