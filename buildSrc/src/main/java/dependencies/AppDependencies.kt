package dependencies

object AppDependencies {
    val immutableCollections = Dependency(
        notation = "org.jetbrains.kotlinx:kotlinx-collections-immutable",
        version = Versions.immutableCollections
    )

    object Ktor {
        val core = Dependency(
            notation = "io.ktor:ktor-client-core",
            version = Versions.ktor,
        )

        val engine = Dependency(
            notation = "io.ktor:ktor-client-cio",
            version = Versions.ktor
        )

        val negotiation = Dependency(
            notation = "io.ktor:ktor-client-content-negotiation",
            version = Versions.ktor
        )

        val serialization = Dependency(
            notation = "io.ktor:ktor-serialization-kotlinx-json",
            version = Versions.ktor
        )

        val logging = Dependency(
            notation = "io.ktor:ktor-client-logging",
            version = Versions.ktor
        )
    }

    object Scout {
        val core = Dependency(
            notation = "com.yandex.scout:scout-core",
            version = Versions.scout
        )

        val validator = Dependency(
            notation = "com.yandex.scout:scout-validator",
            version = Versions.scout
        )

        val graphCollector = Dependency(
            notation = "com.yandex.scout:classgraph-collector",
            version = Versions.scout
        )
    }

    object Accompanist {
        val permissions = Dependency(
            notation = "com.google.accompanist:accompanist-permissions",
            version = Versions.permission,
        )
    }

    object Compose {
        val bom = Dependency(
            notation = "androidx.compose:compose-bom",
            version = Versions.composeBom,
            isPlatform = true,
        )

        val material = Dependency(
            notation = "androidx.compose.material3:material3",
        )

        val preview = Dependency(
            notation = "androidx.compose.ui:ui-tooling-preview",
        )

        val lifeCycleRuntime = Dependency(
            notation = "androidx.lifecycle:lifecycle-runtime-compose",
            version = Versions.lifecycle,
        )

        val activityCore = Dependency(
            notation = "androidx.activity:activity",
            version = Versions.composeActivity,
        )

        val activity = Dependency(
            notation = "androidx.activity:activity-compose",
            version = Versions.composeActivity,
        )

        val viewModel = Dependency(
            notation = "androidx.lifecycle:lifecycle-viewmodel-compose",
            version = Versions.composeViewModel,
        )

        val navigation = Dependency(
            notation = "androidx.navigation:navigation-compose",
            version = Versions.composeNavigation,
        )

        val debugPreview = Dependency(
            notation = "androidx.compose.ui:ui-tooling",
        )
    }

    object Splash {
        val core = Dependency(
            notation = "androidx.core:core-splashscreen",
            version = Versions.splash,
        )
    }

    object Coroutines {
        val core = Dependency(
            notation = "org.jetbrains.kotlinx:kotlinx-coroutines-core",
            version = Versions.coroutines,
        )

        val android = Dependency(
            notation = "org.jetbrains.kotlinx:kotlinx-coroutines-android",
            version = Versions.coroutines,
        )
    }

    object Ktx {
        val core = Dependency(
            notation = "androidx.core:core-ktx",
            version = Versions.coreKtx,
        )
    }

    object Coil {
        val compose = Dependency(
            notation = "io.coil-kt:coil-compose",
            version = Versions.coil,
        )

        val gif = Dependency(
            notation = "io.coil-kt:coil-gif",
            version = Versions.coil,
        )
    }

    object Room {
        val runtime = Dependency(
            notation = "androidx.room:room-runtime",
            version = Versions.room,
        )

        val compiler = Dependency(
            notation = "androidx.room:room-compiler",
            version = Versions.room,
        )

        val ktx = Dependency(
            notation = "androidx.room:room-ktx",
            version = Versions.room,
        )
    }

    object Orbit {
        val viewModel = Dependency(
            notation = "org.orbit-mvi:orbit-viewmodel",
            version = Versions.orbit,
        )

        val compose = Dependency(
            notation = "org.orbit-mvi:orbit-compose",
            version = Versions.orbit,
        )
    }

    object Testing {
        val junit = Dependency(
            notation = "junit:junit",
            version = Versions.junit,
        )

        val androidJunitExtensions = Dependency(
            notation = "androidx.test.ext:junit",
            version = Versions.andoridJunitExtensions,
        )

        val espresso = Dependency(
            notation = "androidx.test.espresso:espresso-core",
            version = Versions.espresso,
        )

        val composeJunit = Dependency(
            notation = "androidx.compose.ui:ui-test-junit4",
        )

        val composeManifest = Dependency(
            notation = "androidx.compose.ui:ui-test-manifest",
        )

        val mockitoCore = Dependency(
            notation = "org.mockito:mockito-core",
            version = Versions.mockitoCore,
        )

        val mockitoKotlin = Dependency(
            notation = "org.mockito.kotlin:mockito-kotlin",
            version = Versions.mockitoKotlin,
        )

        val composeNavigation = Dependency(
            notation = "androidx.navigation:navigation-testing",
            version = Versions.composeNavigation,
        )
    }

    object Versions {
        const val scout = "0.9.3"
        const val coreKtx = "1.12.0"
        const val coroutines = "1.7.3"
        const val detekt = "1.23.5"
        const val composeBom = "2024.02.00"
        const val composeActivity = "1.8.2"
        const val composeViewModel = "2.7.0"
        const val composeNavigation = "2.7.7"
        const val immutableCollections = "0.3.7"
        const val lifecycle = "2.7.0"
        const val ktor = "2.3.8"
        const val coil = "2.5.0"
        const val permission = "0.34.0"
        const val room = "2.6.1"
        const val orbit = "6.1.0"
        const val junit = "4.13.2"
        const val andoridJunitExtensions = "1.1.5"
        const val espresso = "3.5.1"
        const val splash = "1.0.1"
        const val mockitoCore = "5.10.0"
        const val mockitoKotlin = "5.2.1"
    }
}