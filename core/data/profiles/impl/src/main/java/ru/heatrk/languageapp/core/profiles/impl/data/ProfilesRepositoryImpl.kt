package ru.heatrk.languageapp.core.profiles.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.core.profiles.api.domain.Profile
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository

internal class ProfilesRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val supabaseClient: SupabaseClient,
) : ProfilesRepository {
    override suspend fun createProfile(profile: Profile): Unit =
        withContext(dispatcher) {
            supabaseClient.postgrest.from("profiles")
                .upsert(
                    value = ProfileData(
                        id = profile.id,
                        email = profile.email,
                        firstName = profile.firstName,
                        lastName = profile.lastName,
                        totalScore = profile.totalScore,
                        avatarUrl = profile.avatarUrl,
                    ),
                    onConflict = "id",
                    ignoreDuplicates = true
                )
        }

    override suspend fun getCurrentProfile(): Profile =
        withContext(dispatcher) {
            val currentUserInfo = supabaseClient.auth.currentUserOrNull()
                ?: throw IllegalStateException("No current user found")

            val profileData = supabaseClient.postgrest.from("profiles")
                .select {
                    filter {
                        eq("id", currentUserInfo.id)
                    }
                }
                .decodeSingle<ProfileData>()

            Profile(
                id = currentUserInfo.id,
                email = currentUserInfo.email,
                firstName = profileData.firstName,
                lastName = profileData.lastName,
                totalScore = profileData.totalScore,
                avatarUrl = profileData.avatarUrl,
            )
        }
}
