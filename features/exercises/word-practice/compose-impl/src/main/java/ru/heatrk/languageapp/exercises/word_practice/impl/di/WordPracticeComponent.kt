package ru.heatrk.languageapp.exercises.word_practice.impl.di

import androidx.lifecycle.ViewModelProvider
import scout.Component

object WordPracticeComponent : Component(wordPracticeScope) {
    val wordPracticeViewModelFactory: ViewModelProvider.Factory
        get() = get<WordPracticeViewModelFactory>().instance
}
