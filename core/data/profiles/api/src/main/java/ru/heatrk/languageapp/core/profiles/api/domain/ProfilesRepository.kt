package ru.heatrk.languageapp.core.profiles.api.domain

interface ProfilesRepository {
    suspend fun createProfile(profile: Profile)
    suspend fun getCurrentProfile(): Profile
    suspend fun getLeaderboard(count: Long): List<Profile>
    suspend fun updateCurrentProfileAvatar(
        avatarBytes: ByteArray,
        extension: String,
    )
}
