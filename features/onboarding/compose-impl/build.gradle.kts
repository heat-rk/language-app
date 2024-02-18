import dependencies.AppDependencies
import dependencies.ImplementationType

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.heatrk.languageapp.onboarding.impl"

    compileSdk = AppConfig.Sdk.compile

    defaultConfig {
        minSdk = AppConfig.Sdk.min
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")

            AppConfig.buildConfigFields.forEach { field ->
                buildConfigField(field.type, field.name, "\"${field.releaseValue}\"")
            }
        }

        debug {
            AppConfig.buildConfigFields.forEach { field ->
                buildConfigField(field.type, field.name, "\"${field.debugValue}\"")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.composeKotlinCompilerExtensionVersion
    }
}

dependencies {
    modules(
        ":features:onboarding:api",
        ":features:login:api",
        ":core:navigation:api",
        ":core:design",
        ":core:coroutines:dispatchers",
        ":common:utils",
    )

    androidTestModules(
        ":core:navigation:compose-impl",
        ":core:navigation:compose-test",
        ":features:login:compose-impl",
    )

    dependency(AppDependencies.Scout.core)
    dependency(AppDependencies.Orbit.viewModel)
    dependency(AppDependencies.Orbit.compose)
    dependency(AppDependencies.Coroutines.core)
    dependency(AppDependencies.Coroutines.android)
    dependency(AppDependencies.Compose.bom)
    dependency(AppDependencies.Compose.viewModel)
    dependency(AppDependencies.Compose.lifeCycleRuntime)
    dependency(AppDependencies.Compose.material)
    dependency(AppDependencies.Compose.preview)
    dependency(AppDependencies.Compose.debugPreview)
    dependency(AppDependencies.Compose.bom, forcedImplementationType = ImplementationType.ANDROID_TEST)
    dependency(AppDependencies.Testing.composeJunit)
    dependency(AppDependencies.Testing.composeManifest)
    dependency(AppDependencies.Testing.mockitoCore)
    dependency(AppDependencies.Testing.mockitoKotlin)
    dependency(AppDependencies.Testing.composeNavigation)
    dependency(AppDependencies.Compose.navigation, forcedImplementationType = ImplementationType.ANDROID_TEST)
}