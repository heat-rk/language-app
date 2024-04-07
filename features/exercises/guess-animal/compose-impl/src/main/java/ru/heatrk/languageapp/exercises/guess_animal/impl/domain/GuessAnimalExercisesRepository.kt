package ru.heatrk.languageapp.exercises.guess_animal.impl.domain

internal interface GuessAnimalExercisesRepository {
    suspend fun fetchRandomExercise(): GuessAnimalExercise
    suspend fun fetchAnswer(exerciseId: String): String
}
