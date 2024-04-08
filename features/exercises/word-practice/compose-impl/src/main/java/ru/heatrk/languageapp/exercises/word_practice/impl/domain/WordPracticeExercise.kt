package ru.heatrk.languageapp.exercises.word_practice.impl.domain

data class WordPracticeExercise(
    val id: String,
    val word: String,
    val wordTranscription: String,
    val answers: List<String>,
)
