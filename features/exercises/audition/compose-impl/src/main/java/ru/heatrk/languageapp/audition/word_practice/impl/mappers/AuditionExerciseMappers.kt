package ru.heatrk.languageapp.audition.word_practice.impl.mappers

import ru.heatrk.languageapp.audition.word_practice.impl.data.AuditionExerciseData
import ru.heatrk.languageapp.audition.word_practice.impl.domain.AuditionExercise

fun AuditionExerciseData.toDomain() =
    AuditionExercise(
        id = id,
        word = word,
        wordTranscription = wordTranscription,
    )
