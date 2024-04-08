package ru.heatrk.languageapp.exercises.word_practice.impl.domain

internal interface WordPracticeExercisesRepository {
    suspend fun fetchRandomExercise(): WordPracticeExercise
    suspend fun fetchAnswer(exerciseId: String): String
}
