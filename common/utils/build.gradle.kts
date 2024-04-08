import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.heatrk.languageapp.common.utils"

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

            AppConfig.buildConfigFields(project).forEach { field ->
                buildConfigField(field.type, field.name, "\"${field.releaseValue}\"")
            }
        }

        debug {
            AppConfig.buildConfigFields(project).forEach { field ->
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
    dependencies(
        AppDependencies.Compose.bom,
        AppDependencies.Compose.material,
        AppDependencies.Compose.navigation,
        AppDependencies.Ktx.core,
        AppDependencies.Coil.compose,
        AppDependencies.Orbit.viewModel,
    )
}