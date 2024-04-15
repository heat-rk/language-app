package ru.heatrk.languageapp.audition.word_practice.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.audition.word_practice.impl.domain.AuditionExercise
import ru.heatrk.languageapp.audition.word_practice.impl.domain.AuditionExercisesRepository
import ru.heatrk.languageapp.audition.word_practice.impl.mappers.toDomain

internal class AuditionExerciseRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val dispatcher: CoroutineDispatcher,
) : AuditionExercisesRepository {
    override suspend fun fetchRandomExercise(): AuditionExercise =
        withContext(dispatcher) {
            supabaseClient.postgrest
                .from("random_audition_exercises")
                .select {
                    limit(1)
                    single()
                }
                .decodeAs<AuditionExerciseData>()
                .toDomain()
        }
}
