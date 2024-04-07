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
    ":core:data:cache",
    ":core:data:http-client",
    ":core:data:serialization",
    ":core:data:supabase",
    ":core:data:profiles:api",
    ":core:data:profiles:impl",
    ":features:onboarding:api",
    ":features:onboarding:compose-impl",
    ":features:auth:api",
    ":features:auth:compose-impl",
    ":features:main:api",
    ":features:main:compose-impl",
    ":features:profile:api",
    ":features:profile:compose-impl",
    ":features:exercises:guess-animal:api",
    ":features:exercises:guess-animal:compose-impl",
)
