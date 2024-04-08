package ru.heatrk.languageapp.exercises.word_practice.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.exercises.word_practice.impl.data.WordPracticeExerciseRepositoryImpl
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercisesRepository
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeUseCase
import ru.heatrk.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeViewModel
import scout.Scope
import scout.scope

private var _wordPracticeScope: Scope? = null

internal val wordPracticeScope get() = requireNotNull(_wordPracticeScope)

fun Scope.includeWordPracticeScope() {
    _wordPracticeScope = scope("word_practice_scope") {
        dependsOn(this@includeWordPracticeScope)

        reusable<WordPracticeExercisesRepository> {
            WordPracticeExerciseRepositoryImpl(
                supabaseClient = get(),
                dispatcher = get<IoCoroutineDispatcher>().instance
            )
        }

        reusable<WordPracticeUseCase> {
            WordPracticeUseCase(
                wordPracticeExercisesRepository = get(),
                profilesRepository = get(),
            )
        }

        singleton<WordPracticeViewModelFactory> {
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
}
