package ru.heatrk.languageapp.onboarding.impl.domain

import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.api.domain.models.OnboardingUnit
import ru.heatrk.languageapp.onboarding.impl.data.OnboardingStorage

class OnboardingRepositoryImpl(
    private val onboardingStorage: OnboardingStorage,
) : OnboardingRepository {
    override suspend fun getUnwatchedUnits(): List<OnboardingUnit> {
        return OnboardingUnit.entries - getWatchedUnits()
    }

    override suspend fun saveWatchedUnit(unit: OnboardingUnit) {
        onboardingStorage.saveWatchedUnit(unit.name)
    }

    private suspend fun getWatchedUnits(): Set<OnboardingUnit> {
        return onboardingStorage.getWatchedOnboardingUnits()
            .map { name ->
                OnboardingUnit.valueOf(name)
            }.toSet()
    }
}
