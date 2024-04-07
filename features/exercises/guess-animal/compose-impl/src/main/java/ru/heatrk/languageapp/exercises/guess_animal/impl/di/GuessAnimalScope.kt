package ru.heatrk.languageapp.exercises.guess_animal.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.exercises.guess_animal.impl.data.GuessAnimalExerciseRepositoryImpl
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercisesRepository
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalUseCase
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalViewModel
import scout.Scope
import scout.scope

private var _guessAnimalScope: Scope? = null

internal val guessAnimalScope get() = requireNotNull(_guessAnimalScope)

fun Scope.includeGuessAnimalScope() {
    _guessAnimalScope = scope("guess_animal_scope") {
        dependsOn(this@includeGuessAnimalScope)

        reusable<GuessAnimalExercisesRepository> {
            GuessAnimalExerciseRepositoryImpl(
                supabaseClient = get(),
                dispatcher = get<IoCoroutineDispatcher>().instance
            )
        }

        reusable<GuessAnimalUseCase> {
            GuessAnimalUseCase(
                guessAnimalExercisesRepository = get(),
                profilesRepository = get(),
            )
        }

        singleton<GuessAnimalViewModelFactory> {
            GuessAnimalViewModelFactory(
                viewModelFactory {
                    initializer {
                        GuessAnimalViewModel(
                            router = get(),
                            guessAnimalExercisesRepository = get(),
                            guessAnimal = get(),
                        )
                    }
                }
            )
        }
    }
}
