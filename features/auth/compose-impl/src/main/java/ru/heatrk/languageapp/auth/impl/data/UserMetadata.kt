package ru.heatrk.languageapp.auth.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMetadata(
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("total_score")
    val totalScore: Int = 0,
)
