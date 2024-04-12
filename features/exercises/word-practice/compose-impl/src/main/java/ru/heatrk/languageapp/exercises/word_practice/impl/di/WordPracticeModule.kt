package ru.heatrk.languageapp.exercises.word_practice.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.IoCoroutineDispatcherQualifier
import ru.heatrk.languageapp.exercises.word_practice.impl.data.WordPracticeExerciseRepositoryImpl
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercisesRepository
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeUseCase
import ru.heatrk.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeViewModel

internal val WordPracticeViewModelFactoryQualifier =
    qualifier("WordPracticeViewModelFactory")

val wordPracticeModule = module {
    factory<WordPracticeExercisesRepository> {
        WordPracticeExerciseRepositoryImpl(
            supabaseClient = get(),
            dispatcher = get(IoCoroutineDispatcherQualifier)
        )
    }

    factory<WordPracticeUseCase> {
        WordPracticeUseCase(
            wordPracticeExercisesRepository = get(),
            profilesRepository = get(),
        )
    }

    single<ViewModelProvider.Factory>(WordPracticeViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                WordPracticeViewModel(
                    router = get(),
                    wordPracticeExercisesRepository = get(),
                    wordPractice = get(),
                )
            }
        }
    }
}
