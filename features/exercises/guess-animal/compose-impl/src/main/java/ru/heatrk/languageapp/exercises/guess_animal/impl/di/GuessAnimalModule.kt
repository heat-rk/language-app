package ru.heatrk.languageapp.exercises.guess_animal.impl.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.heatrk.languageapp.core.coroutines.dispatchers.di.IoCoroutineDispatcherQualifier
import ru.heatrk.languageapp.exercises.guess_animal.impl.data.GuessAnimalExerciseRepositoryImpl
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercisesRepository
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalUseCase
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalViewModel

internal val GuessAnimalViewModelFactoryQualifier =
    qualifier("GuessAnimalViewModelFactory")

val guessAnimalModule = module {
    factory<GuessAnimalExercisesRepository> {
        GuessAnimalExerciseRepositoryImpl(
            supabaseClient = get(),
            dispatcher = get(IoCoroutineDispatcherQualifier)
        )
    }

    factory<GuessAnimalUseCase> {
        GuessAnimalUseCase(
            guessAnimalExercisesRepository = get(),
            profilesRepository = get(),
        )
    }

    single<ViewModelProvider.Factory>(GuessAnimalViewModelFactoryQualifier) {
        viewModelFactory {
            initializer {
                GuessAnimalViewModel(
                    router = get(),
                    guessAnimalExercisesRepository = get(),
                    guessAnimal = get(),
                )
            }
        }
    }
}
