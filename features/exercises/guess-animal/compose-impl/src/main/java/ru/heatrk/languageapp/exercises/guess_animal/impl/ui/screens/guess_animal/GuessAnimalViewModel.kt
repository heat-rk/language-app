package ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.common.utils.states.ProcessingState
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.design.utils.withReturnToNone
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.EmptyInputException
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercise
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercisesRepository
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalUseCase
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.Intent
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.SideEffect
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.State
import ru.heatrk.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class GuessAnimalViewModel(
    private val router: Router,
    private val guessAnimalExercisesRepository: GuessAnimalExercisesRepository,
    private val guessAnimal: GuessAnimalUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Loading
    )

    private var currentExercise: GuessAnimalExercise? = null
    private var currentStreak = 0

    init {
        loadNextExercise()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnAnswerTextChange ->
                onAnswerTextChange(intent.text)
            Intent.OnCheckButtonClick ->
                onCheckButtonClick()
            Intent.OnNextButtonClick ->
                onNextButtonClick()
            Intent.OnTryAgainButtonClick ->
                onTryAgainButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()

        }
    }

    private fun loadNextExercise() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { State.Loading }

                val exercise = guessAnimalExercisesRepository
                    .fetchRandomExercise()
                    .also { currentExercise = it }

                reduce {
                    State.Resolving(image = painterRes(exercise.imageUrl))
                }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                reduce { State.Error }
            }
        )
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private suspend fun IntentBody.onAnswerTextChange(text: String) {
        reduce {
            (state as State.Resolving).copy(
                answer = text
            )
        }
    }

    private suspend fun IntentBody.onCheckButtonClick() {
        viewModelScope.launchSafe(
            block = {
                reduce {
                    (state as State.Resolving).copy(
                        checkingAnswerState = ProcessingState.InProgress
                    )
                }

                postSideEffect(SideEffect.CloseKeyboard)

                val guessResult = guessAnimal(
                    streak = currentStreak,
                    exerciseId = requireNotNull(currentExercise).id,
                    answer = (state as State.Resolving).answer,
                )

                if (guessResult.isCorrect) {
                    currentStreak += 1
                    reduce { State.CorrectAnswer }
                } else {
                    currentStreak = 0
                    reduce { State.IncorrectAnswer(correctAnswer = guessResult.correctAnswer) }
                }
            },
            onError = { throwable ->
                withReturnToNone(startWith = ProcessingState.Error) { recoveringState ->
                    reduce {
                        (state as State.Resolving).copy(
                            checkingAnswerState = recoveringState
                        )
                    }
                }

                when (throwable) {
                    is EmptyInputException -> Unit
                    else -> postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))
                }
            }
        )
    }

    private fun onNextButtonClick() {
        loadNextExercise()
    }

    private suspend fun IntentBody.onTryAgainButtonClick() {
        reduce {
            State.Resolving(image = painterRes(requireNotNull(currentExercise).imageUrl))
        }
    }
}
