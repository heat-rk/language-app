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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
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