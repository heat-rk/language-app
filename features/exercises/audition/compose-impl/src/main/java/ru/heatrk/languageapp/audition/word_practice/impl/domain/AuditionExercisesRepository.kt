package ru.heatrk.languageapp.audition.word_practice.impl.domain

internal interface AuditionExercisesRepository {
    suspend fun fetchRandomExercise(): AuditionExercise
}
