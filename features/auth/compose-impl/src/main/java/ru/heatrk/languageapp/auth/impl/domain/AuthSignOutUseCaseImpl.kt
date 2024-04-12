package ru.heatrk.languageapp.auth.impl.domain

import ru.heatrk.languageapp.auth.api.domain.AuthSignOutUseCase
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository

internal class AuthSignOutUseCaseImpl(
    private val authRepository: AuthRepository,
    private val profilesRepository: ProfilesRepository,
) : AuthSignOutUseCase {
    override suspend fun invoke() {
        profilesRepository.clearCachedUserProfile()
        authRepository.signOut()
    }
}
