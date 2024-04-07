package ru.heatrk.languageapp.exercises.guess_animal.impl.di

import androidx.lifecycle.ViewModelProvider
import scout.Component

object GuessAnimalComponent : Component(guessAnimalScope) {
    val guessAnimalViewModelFactory: ViewModelProvider.Factory
        get() = get<GuessAnimalViewModelFactory>().instance
}
