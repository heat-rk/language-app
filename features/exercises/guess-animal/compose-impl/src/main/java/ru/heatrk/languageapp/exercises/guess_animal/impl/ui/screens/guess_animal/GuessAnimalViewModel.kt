package ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
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
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercise
import ru.heatrk.languageapp.core.design.R as DesignR
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercisesRepository
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalUseCase
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.State
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.SideEffect
import ru.heatrk.languageapp.exercises.guess_animal.impl.ui.screens.guess_animal.GuessAnimalContract.Intent

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class GuessAnimalViewModel(
    private val router: Router,
    private val guessAnimalExercisesRepository: GuessAnimalExercisesRepository,
    private val guessAnimal: GuessAnimalUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Resolving()
    )

    private var currentExercise: GuessAnimalExercise? = null

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
                reduce { State.Resolving() }

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

                val guessResult = guessAnimal(
                    exerciseId = requireNotNull(currentExercise).id,
                    answer = (state as State.Resolving).answer,
                )

                reduce {
                    if (guessResult.isCorrect) {
                        State.CorrectAnswer
                    } else {
                        State.IncorrectAnswer(correctAnswer = guessResult.correctAnswer)
                    }
                }
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))

                reduce {
                    (state as State.Resolving).copy(
                        checkingAnswerState = ProcessingState.Error
                    )
                }

                delay(PROCESSING_STATE_DELAY_MILLIS)

                reduce {
                    (state as State.Resolving).copy(
                        checkingAnswerState = ProcessingState.None
                    )
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

    companion object {
        private const val PROCESSING_STATE_DELAY_MILLIS = 1000L
    }
}
