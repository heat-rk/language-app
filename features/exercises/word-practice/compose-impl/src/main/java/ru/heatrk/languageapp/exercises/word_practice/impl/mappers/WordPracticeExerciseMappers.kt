package ru.heatrk.languageapp.exercises.word_practice.impl.mappers

import ru.heatrk.languageapp.exercises.word_practice.impl.data.WordPracticeExerciseData
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercise

fun WordPracticeExerciseData.toDomain() =
    WordPracticeExercise(
        id = id,
        word = word,
        wordTranscription = wordTranscription,
        answers = answers,
    )
