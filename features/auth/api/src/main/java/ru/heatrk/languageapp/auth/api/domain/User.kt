package ru.heatrk.languageapp.auth.api.domain

data class User(
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val totalScore: Int,
    val avatarUrl: String?,
)
