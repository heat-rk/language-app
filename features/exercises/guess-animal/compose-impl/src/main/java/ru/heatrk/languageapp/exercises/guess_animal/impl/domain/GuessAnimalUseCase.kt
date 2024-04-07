package ru.heatrk.languageapp.exercises.guess_animal.impl.domain

import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository

internal class GuessAnimalUseCase(
    private val guessAnimalExercisesRepository: GuessAnimalExercisesRepository,
    private val profilesRepository: ProfilesRepository,
) {
    suspend operator fun invoke(
        streak: Int,
        exerciseId: String,
        answer: String,
    ): Result {
        val correctAnswer = guessAnimalExercisesRepository
            .fetchAnswer(exerciseId)

        val isAnswerCorrect = answer.trim().equals(correctAnswer.trim(), ignoreCase = true)

        if (isAnswerCorrect) {
            val streakPoints = if (streak > 0) STREAK_MULTIPLIER * (streak + 1) else 0f

            profilesRepository
                .increaseProfileTotalPoints(
                    points = CORRECT_ANSWER_POINTS_COUNT + streakPoints
                )
        }

        return Result(
            isCorrect = isAnswerCorrect,
            correctAnswer = correctAnswer.trim()
        )
    }

    data class Result(
        val isCorrect: Boolean,
        val correctAnswer: String,
    )

    companion object {
        private const val CORRECT_ANSWER_POINTS_COUNT = 1f
        private const val STREAK_MULTIPLIER = 0.2f
    }
}
