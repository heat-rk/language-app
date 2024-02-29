import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidLibrary)
    id(AppPlugins.androidKotlin)
}

android {
    namespace = "ru.heatrk.languageapp.core.data.http_client"

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
        buildConfig = true
    }
}

dependencies {
    dependencies(
        AppDependencies.Ktor.core,
        AppDependencies.Ktor.engine,
        AppDependencies.Ktor.logging,
        AppDependencies.Ktor.serialization,
        AppDependencies.Ktor.negotiation,
        AppDependencies.Scout.core,
    )
}