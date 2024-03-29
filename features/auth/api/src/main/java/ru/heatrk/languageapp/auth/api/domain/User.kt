package ru.heatrk.languageapp.auth.api.domain

data class User(
    val id: String,
    val avatarUrl: String?,
    val firstName: String?,
    val lastName: String?,
)
