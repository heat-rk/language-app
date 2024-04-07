package ru.heatrk.languageapp.exercises.guess_animal.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercise
import ru.heatrk.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercisesRepository
import ru.heatrk.languageapp.exercises.guess_animal.impl.mappers.toDomain

class GuessAnimalExerciseRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val dispatcher: CoroutineDispatcher,
) : GuessAnimalExercisesRepository {
    override suspend fun fetchRandomExercise(): GuessAnimalExercise =
        withContext(dispatcher) {
            supabaseClient.postgrest
                .from("random_animals_exercises")
                .select {
                    limit(1)
                    single()
                }
                .decodeAs<GuessAnimalExerciseData>()
                .toDomain()
        }

    override suspend fun fetchAnswer(exerciseId: String): String =
        withContext(dispatcher) {
            supabaseClient.postgrest
                .from("animals_exercises_answers")
                .select {
                    filter {
                        eq(column = "id", exerciseId)
                    }
                }
                .decodeSingle<GuessAnimalExerciseAnswerData>()
                .answer
        }
}
