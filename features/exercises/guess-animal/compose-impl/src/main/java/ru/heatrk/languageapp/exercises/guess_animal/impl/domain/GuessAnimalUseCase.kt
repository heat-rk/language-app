package ru.heatrk.languageapp.exercises.guess_animal.impl.domain

internal class GuessAnimalUseCase(
    private val guessAnimalExercisesRepository: GuessAnimalExercisesRepository
) {
    suspend operator fun invoke(
        exerciseId: String,
        answer: String,
    ): Result {
        val correctAnswer = guessAnimalExercisesRepository
            .fetchAnswer(exerciseId)

        return Result(
            isCorrect = answer.trim().equals(correctAnswer.trim(), ignoreCase = true),
            correctAnswer = correctAnswer.trim()
        )
    }

    data class Result(
        val isCorrect: Boolean,
        val correctAnswer: String,
    )
}
