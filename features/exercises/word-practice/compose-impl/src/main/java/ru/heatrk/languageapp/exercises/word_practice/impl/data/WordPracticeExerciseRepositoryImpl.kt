package ru.heatrk.languageapp.exercises.word_practice.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercise
import ru.heatrk.languageapp.exercises.word_practice.impl.domain.WordPracticeExercisesRepository
import ru.heatrk.languageapp.exercises.word_practice.impl.mappers.toDomain

class WordPracticeExerciseRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val dispatcher: CoroutineDispatcher,
) : WordPracticeExercisesRepository {
    override suspend fun fetchRandomExercise(): WordPracticeExercise =
        withContext(dispatcher) {
            supabaseClient.postgrest
                .from("random_word_practice_exercises")
                .select {
                    limit(1)
                    single()
                }
                .decodeAs<WordPracticeExerciseData>()
                .toDomain()
        }

    override suspend fun fetchAnswer(exerciseId: String): String =
        withContext(dispatcher) {
            supabaseClient.postgrest
                .from("word_practice_exercises_answers")
                .select {
                    filter {
                        eq(column = "id", exerciseId)
                    }
                }
                .decodeSingle<WordPracticeExerciseAnswerData>()
                .answer
        }
}
