object AppDependencies {
    const val immutableCollections = "org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.immutableCollections}"

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val engine = "io.ktor:ktor-client-cio:${Versions.ktor}"
        const val negotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        const val logging = "io.ktor:ktor-client-logging:${Versions.ktor}"

        val allImplementations = arrayOf(core, engine, negotiation, serialization, logging)
    }

    object Scout {
        const val core = "com.yandex.scout:scout-core:${Versions.scout}"
        val allImplementations = arrayOf(core)
    }

    object Accompanist {
        const val permissions = "com.google.accompanist:accompanist-permissions:${Versions.permission}"
        val allImplementations = arrayOf(permissions)
    }

    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"
        const val material = "androidx.compose.material3:material3"
        const val preview = "androidx.compose.ui:ui-tooling-preview"
        const val lifeCycleRuntime = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycle}"
        const val activityCore = "androidx.activity:activity:${Versions.composeActivity}"
        const val activity = "androidx.activity:activity-compose:${Versions.composeActivity}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeViewModel}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.composeNavigation}"

        const val debugPreview = "androidx.compose.ui:ui-tooling"

        val platformImplementations = arrayOf(bom)
        val allImplementations = arrayOf(material, preview, activityCore, activity, viewModel, navigation, lifeCycleRuntime)
        val debugImplementations = arrayOf(debugPreview)
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

        val allImplementations = arrayOf(core, android)
    }

    object Ktx {
        const val core = "androidx.core:core-ktx:${Versions.coreKtx}"
        val allImplementations = arrayOf(core)
    }

    object Coil {
        const val compose = "io.coil-kt:coil-compose:${Versions.coil}"
        const val gif = "io.coil-kt:coil-gif:${Versions.coil}"
        val allImplementations = arrayOf(compose, gif)
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
        val allImplementations = arrayOf(runtime, ktx)
    }

    object Orbit {
        const val viewModel = "org.orbit-mvi:orbit-viewmodel:${Versions.orbit}"
        const val compose = "org.orbit-mvi:orbit-compose:${Versions.orbit}"
        val allImplementations = arrayOf(viewModel, compose)
    }

    object Testing {
        const val junit = "junit:junit:${Versions.junit}"
        const val androidJunitExtensions = "androidx.test.ext:junit:${Versions.andoridJunitExtensions}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"

        val testImplementations = arrayOf(junit)
        val androidTestImplementations = arrayOf(androidJunitExtensions, espresso)
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
    }
}