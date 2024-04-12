package ru.heatrk.languageapp.core.profiles.api.domain

import kotlinx.coroutines.flow.Flow

interface ProfilesRepository {
    suspend fun createProfile(profile: Profile)
    suspend fun fetchCurrentProfile(): Profile
    suspend fun clearCachedUserProfile()
    suspend fun observeCurrentProfile(reload: Boolean = false): Flow<Profile>
    suspend fun getLeaderboard(count: Long): List<Profile>
    suspend fun updateCurrentProfileAvatar(
        avatarBytes: ByteArray,
        extension: String,
    )
    suspend fun increaseProfileTotalPoints(points: Float)
}
