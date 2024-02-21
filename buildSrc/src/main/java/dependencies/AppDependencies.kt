package dependencies

object AppDependencies {
    val immutableCollections = defaultDependency(
        notation = "org.jetbrains.kotlinx:kotlinx-collections-immutable",
        version = Versions.immutableCollections
    )

    object Ktor {
        val core = defaultDependency(
            notation = "io.ktor:ktor-client-core",
            version = Versions.ktor
        )

        val engine = defaultDependency(
            notation = "io.ktor:ktor-client-cio",
            version = Versions.ktor
        )

        val negotiation = defaultDependency(
            notation = "io.ktor:ktor-client-content-negotiation",
            version = Versions.ktor
        )

        val serialization = defaultDependency(
            notation = "io.ktor:ktor-serialization-kotlinx-json",
            version = Versions.ktor
        )

        val logging = defaultDependency(
            notation = "io.ktor:ktor-client-logging",
            version = Versions.ktor
        )
    }

    object Scout {
        val core = defaultDependency(
            notation = "com.yandex.scout:scout-core",
            version = Versions.scout
        )

        val validator = testDependency(
            notation = "com.yandex.scout:scout-validator",
            version = Versions.scout
        )

        val graphCollector = testDependency(
            notation = "com.yandex.scout:classgraph-collector",
            version = Versions.scout
        )
    }

    object Accompanist {
        val permissions = defaultDependency(
            notation = "com.google.accompanist:accompanist-permissions",
            version = Versions.permission,
        )
    }

    object Compose {
        val bom = defaultDependency(
            notation = "androidx.compose:compose-bom",
            version = Versions.composeBom,
            isPlatform = true,
        )

        val material = defaultDependency(
            notation = "androidx.compose.material3:material3",
        )

        val preview = defaultDependency(
            notation = "androidx.compose.ui:ui-tooling-preview",
        )

        val lifeCycleRuntime = defaultDependency(
            notation = "androidx.lifecycle:lifecycle-runtime-compose",
            version = Versions.lifecycle,
        )

        val activityCore = defaultDependency(
            notation = "androidx.activity:activity",
            version = Versions.composeActivity,
        )

        val activity = defaultDependency(
            notation = "androidx.activity:activity-compose",
            version = Versions.composeActivity,
        )

        val viewModel = defaultDependency(
            notation = "androidx.lifecycle:lifecycle-viewmodel-compose",
            version = Versions.composeViewModel,
        )

        val navigation = defaultDependency(
            notation = "androidx.navigation:navigation-compose",
            version = Versions.composeNavigation,
        )

        val debugPreview = debugDependency(
            notation = "androidx.compose.ui:ui-tooling",
        )
    }

    object Splash {
        val core = defaultDependency(
            notation = "androidx.core:core-splashscreen",
            version = Versions.splash,
        )
    }

    object Coroutines {
        val core = defaultDependency(
            notation = "org.jetbrains.kotlinx:kotlinx-coroutines-core",
            version = Versions.coroutines,
        )

        val android = defaultDependency(
            notation = "org.jetbrains.kotlinx:kotlinx-coroutines-android",
            version = Versions.coroutines,
        )
    }

    object Ktx {
        val core = defaultDependency(
            notation = "androidx.core:core-ktx",
            version = Versions.coreKtx,
        )
    }

    object Coil {
        val compose = defaultDependency(
            notation = "io.coil-kt:coil-compose",
            version = Versions.coil,
        )

        val gif = defaultDependency(
            notation = "io.coil-kt:coil-gif",
            version = Versions.coil,
        )
    }

    object Room {
        val runtime = defaultDependency(
            notation = "androidx.room:room-runtime",
            version = Versions.room,
        )

        val compiler = kaptDependency(
            notation = "androidx.room:room-compiler",
            version = Versions.room,
        )

        val ktx = defaultDependency(
            notation = "androidx.room:room-ktx",
            version = Versions.room,
        )
    }

    object Orbit {
        val viewModel = defaultDependency(
            notation = "org.orbit-mvi:orbit-viewmodel",
            version = Versions.orbit,
        )

        val compose = defaultDependency(
            notation = "org.orbit-mvi:orbit-compose",
            version = Versions.orbit,
        )
    }

    object Testing {
        val junit = testDependency(
            notation = "junit:junit",
            version = Versions.junit,
        )

        val androidJunitExtensions = androidTestDependency(
            notation = "androidx.test.ext:junit",
            version = Versions.andoridJunitExtensions,
        )

        val espresso = androidTestDependency(
            notation = "androidx.test.espresso:espresso-core",
            version = Versions.espresso,
        )

        val composeJunit = androidTestDependency(
            notation = "androidx.compose.ui:ui-test-junit4",
        )

        val composeManifest = debugDependency(
            notation = "androidx.compose.ui:ui-test-manifest",
        )

        val mockitoCore = androidTestDependency(
            notation = "org.mockito:mockito-core",
            version = Versions.mockitoCore,
        )

        val mockitoKotlin = androidTestDependency(
            notation = "org.mockito.kotlin:mockito-kotlin",
            version = Versions.mockitoKotlin,
        )

        val composeNavigation = androidTestDependency(
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