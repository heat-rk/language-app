package ru.heatrk.languageapp.core.profiles.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.core.data.profiles.impl.BuildConfig
import ru.heatrk.languageapp.core.profiles.api.domain.Profile
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.core.profiles.impl.mappers.toData
import ru.heatrk.languageapp.core.profiles.impl.mappers.toDomain

internal class ProfilesRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val supabaseClient: SupabaseClient,
) : ProfilesRepository {
    override suspend fun createProfile(profile: Profile): Unit =
        withContext(dispatcher) {
            supabaseClient.postgrest.from("profiles")
                .upsert(
                    value = profile.toData(),
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

            profileData.toDomain()
        }

    override suspend fun getLeaderboard(count: Long): List<Profile> =
        withContext(dispatcher) {
            val leaderboardData = supabaseClient.postgrest
                .from("profiles")
                .select {
                    order(
                        column = "total_score",
                        order = Order.DESCENDING
                    )

                    limit(count)
                }
                .decodeList<ProfileData>()

            leaderboardData.map(ProfileData::toDomain)
        }

    override suspend fun updateCurrentProfileAvatar(
        avatarBytes: ByteArray,
        extension: String,
    ): Unit =
        withContext(dispatcher) {
            val profile = getCurrentProfile()

            val path = supabaseClient.storage.from("avatars")
                .upload(
                    path = "${profile.id}/avatar.${extension}",
                    data = avatarBytes,
                    upsert = true,
                )

            val avatarUrl = "${BuildConfig.SUPABASE_STORAGE_URL}/$path"

            supabaseClient.postgrest.from("profiles")
                .update(
                    value = profile
                        .copy(avatarUrl = avatarUrl)
                        .toData()
                ) {
                    filter {
                        eq("id", profile.id)
                    }
                }
        }
}
