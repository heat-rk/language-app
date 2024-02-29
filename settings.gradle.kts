pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LanguageApp"

include(
    ":app",
    ":common:utils",
    ":core:design",
    ":core:navigation:api",
    ":core:navigation:compose-impl",
    ":core:navigation:compose-test",
    ":core:coroutines:dispatchers",
    ":core:coroutines:scopes",
    ":core:data:db",
    ":core:data:http-client",
    ":core:data:supabase",
    ":features:onboarding:api",
    ":features:onboarding:compose-impl",
    ":features:auth:api",
    ":features:auth:compose-impl",
    ":features:main:api",
    ":features:main:compose-impl",
)
