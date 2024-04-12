package ru.heatrk.languageapp.exercises.guess_animal.impl.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.IoCoroutineDispatcher
import ru.heatrk.languageapp.exercises.guess_animal.impl.data.GuessAnimalExerciseRepositoryImpl
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercisesRepository
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalUseCase
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalViewModel

val guessAnimalModule = module {
    factory<GuessAnimalExercisesRepository> {
        GuessAnimalExerciseRepositoryImpl(
            supabaseClient = get(),
            dispatcher = get<IoCoroutineDispatcher>().instance
        )
    }

    factory<GuessAnimalUseCase> {
        GuessAnimalUseCase(
            guessAnimalExercisesRepository = get(),
            profilesRepository = get(),
        )
    }

    single<GuessAnimalViewModelFactory> {
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
