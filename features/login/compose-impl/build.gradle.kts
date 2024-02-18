import dependencies.AppDependencies
import dependencies.ImplementationType

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.heatrk.languageapp.login.impl"

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
        ":features:login:api",
        ":core:navigation:api",
        ":core:design",
        ":common:utils",
    )

    dependency(AppDependencies.Scout.core)
    dependency(AppDependencies.Orbit.viewModel)
    dependency(AppDependencies.Orbit.compose)
    dependency(AppDependencies.Coroutines.core)
    dependency(AppDependencies.Coroutines.android)
    dependency(AppDependencies.Compose.bom)
    dependency(AppDependencies.Compose.material)
    dependency(AppDependencies.Compose.viewModel)
    dependency(AppDependencies.Compose.preview)
    dependency(AppDependencies.Compose.debugPreview)
}