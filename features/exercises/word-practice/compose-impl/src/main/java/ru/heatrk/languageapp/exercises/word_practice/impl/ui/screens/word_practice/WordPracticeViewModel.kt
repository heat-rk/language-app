package ru.heatrk.languageapp.exercises.word_practice.impl.ui.screens.word_practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.states.ProcessingState
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercise
import ru.heatrk.languageapp.core.design.R as DesignR
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercisesRepository
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeUseCase
import ru.heatrk.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.State
import ru.heatrk.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.SideEffect
import ru.heatrk.languageapp.exercises.word_practice.impl.ui.screens.word_practice.WordPracticeContract.Intent

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class WordPracticeViewModel(
    private val router: Router,
    private val wordPracticeExercisesRepository: WordPracticeExercisesRepository,
    private val wordPractice: WordPracticeUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State.Loading
    )

    private var currentExercise: WordPracticeExercise? = null
    private var currentStreak = 0

    init {
        loadNextExercise()
    }

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnAnswerClick ->
                onAnswerClickChange(intent.answer)
            Intent.OnCheckButtonClick ->
                onCheckButtonClick()
            Intent.OnNextButtonClick ->
                onNextButtonClick()
            Intent.OnGoBackClick ->
                onGoBackClick()
        }
    }

    private fun loadNextExercise() = intent {
        viewModelScope.launchSafe(
            block = {
                reduce { State.Loading }

                val exercise = wordPracticeExercisesRepository
                    .fetchRandomExercise()
                    .also { currentExercise = it }

                reduce {
                    State.Resolving(
                        word = exercise.word,
                        wordTranscription = exercise.wordTranscription,
                        answers = exercise.answers
                            .map { word -> WordPracticeAnswerItem(word = word) }
                            .toImmutableList(),
                    )
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

    private suspend fun IntentBody.onAnswerClickChange(clickedAnswer: WordPracticeAnswerItem) {
        reduce {
            (state as State.Resolving).let { state ->
                state.copy(answers = state.answers.withUserSelection(clickedAnswer.word))
            }
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

                val selectedAnswer =
                    requireNotNull((state as State.Resolving).answers.getUserSelectedAnswer())

                val exerciseResult = wordPractice(
                    streak = currentStreak,
                    exerciseId = requireNotNull(currentExercise).id,
                    answer = selectedAnswer.word,
                )

                currentStreak = if (exerciseResult.isCorrect) currentStreak + 1 else 0

                reduce {
                    (state as State.Resolving).let { state ->
                        state.copy(
                            isResolved = true,
                            checkingAnswerState = if (exerciseResult.isCorrect) {
                                ProcessingState.Success
                            } else {
                                ProcessingState.Error
                            },
                            answers = state.answers.withCorrectAnswer(
                                userAnswerWord = selectedAnswer.word,
                                correctAnswerWord = exerciseResult.correctAnswer,
                            )
                        )
                    }
                }

                delay(PROCESSING_STATE_DELAY_MILLIS)

                reduce {
                    (state as State.Resolving).copy(
                        checkingAnswerState = ProcessingState.None
                    )
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

    private fun ImmutableList<WordPracticeAnswerItem>.withUserSelection(
        userSelectedWord: String
    ) =
        map { answer ->
            answer.copy(
                selectionType = if (userSelectedWord == answer.word) {
                    WordPracticeAnswerSelectionType.User
                } else {
                    WordPracticeAnswerSelectionType.None
                }
            )
        }.toImmutableList()

    private fun ImmutableList<WordPracticeAnswerItem>.withCorrectAnswer(
        userAnswerWord: String,
        correctAnswerWord: String,
    ) =
        map { answer ->
            answer.copy(
                selectionType = when (answer.word) {
                    correctAnswerWord ->
                        WordPracticeAnswerSelectionType.Correct
                    userAnswerWord ->
                        WordPracticeAnswerSelectionType.Incorrect
                    else ->
                        WordPracticeAnswerSelectionType.None
                }
            )
        }.toImmutableList()

    private fun ImmutableList<WordPracticeAnswerItem>.getUserSelectedAnswer() =
        find { answer -> answer.selectionType == WordPracticeAnswerSelectionType.User }

    companion object {
        private const val PROCESSING_STATE_DELAY_MILLIS = 1000L
    }
}
