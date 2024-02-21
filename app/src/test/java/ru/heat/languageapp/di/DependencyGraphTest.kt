package ru.heat.languageapp.di

import org.junit.Test
import scout.collector.classgraph.ComponentCollector
import scout.validator.Validator

class DependencyGraphTest {
    @Test
    fun `check dependency graph`() {
        Validator.configure()
            .withConsistencyCheck()
            .withOverridesCheck()
            .withScopeNamingChecker { name -> name.isNotEmpty() }
            .validate(ComponentCollector())
    }
}
