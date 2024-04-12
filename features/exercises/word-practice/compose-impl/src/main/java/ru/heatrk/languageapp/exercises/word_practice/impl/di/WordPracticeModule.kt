package ru.heatrk.languageapp.exercises.word_practice.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.exercises.word_practice.impl.data.WordPracticeExerciseRepositoryImpl
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercisesRepository
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeUseCase
import ru.heatrk.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeViewModel

val wordPracticeModule = module {
    factory<WordPracticeExercisesRepository> {
        WordPracticeExerciseRepositoryImpl(
            supabaseClient = get(),
            dispatcher = get<IoCoroutineDispatcher>().instance
        )
    }

    factory<WordPracticeUseCase> {
        WordPracticeUseCase(
            wordPracticeExercisesRepository = get(),
            profilesRepository = get(),
        )
    }

    single<WordPracticeViewModelFactory> {
        WordPracticeViewModelFactory(
            viewModelFactory {
                initializer {
                    WordPracticeViewModel(
                        router = get(),
                        wordPracticeExercisesRepository = get(),
                        wordPractice = get(),
                    )
                }
            }
        )
    }
}
