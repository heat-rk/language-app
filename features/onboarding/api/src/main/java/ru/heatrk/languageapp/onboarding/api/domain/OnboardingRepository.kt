package ru.heatrk.languageapp.onboarding.api.domain

import ru.heatrk.languageapp.onboarding.api.domain.models.OnboardingUnit

interface OnboardingRepository {
    suspend fun getUnwatchedUnits(): List<OnboardingUnit>
    suspend fun saveWatchedUnit(unit: OnboardingUnit)
    suspend fun saveWatchedUnits(units: List<OnboardingUnit>)
}
