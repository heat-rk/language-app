package ru.heatrk.languageapp.core.profiles.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.heatrk.languageapp.core.data.cache.InMemoryCacheContainer
import ru.heatrk.languageapp.core.data.profiles.impl.BuildConfig
import ru.heatrk.languageapp.core.profiles.api.domain.Profile
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.core.profiles.impl.mappers.toData
import ru.heatrk.languageapp.core.profiles.impl.mappers.toDomain

internal class ProfilesRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val supabaseClient: SupabaseClient,
    private val inMemoryUserProfileCacheContainer: InMemoryCacheContainer<Profile>,
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

    override suspend fun fetchCurrentProfile(): Profile =
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

    override suspend fun observeCurrentProfile(reload: Boolean): Flow<Profile> {
        if (reload) {
            inMemoryUserProfileCacheContainer.value = fetchCurrentProfile()
        }

        return inMemoryUserProfileCacheContainer.valueFlow(
            defaultDataProvider = {
                inMemoryUserProfileCacheContainer.getValueOrSave(fetchCurrentProfile())
            }
        )
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
            val profile = fetchCurrentProfile()

            supabaseClient.storage
                .from("avatars")
                .list(profile.id)
                .forEach { file ->
                    supabaseClient.storage.from("avatars")
                        .delete("${profile.id}/${file.name}")
                }

            val avatarFileName =
                "avatar_${Clock.System.now().toEpochMilliseconds()}.${extension}"

            val path = supabaseClient.storage.from("avatars")
                .upload(
                    path = "${profile.id}/$avatarFileName",
                    data = avatarBytes,
                    upsert = true,
                )

            val avatarUrl = "${BuildConfig.SUPABASE_STORAGE_URL}/$path"

            val updatedProfile = profile.copy(avatarUrl = avatarUrl)

            supabaseClient.postgrest.from("profiles")
                .update(
                    value = updatedProfile.toData()
                ) {
                    filter {
                        eq("id", profile.id)
                    }
                }

            inMemoryUserProfileCacheContainer.value = updatedProfile
        }
}
