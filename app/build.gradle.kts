import dependencies.AppDependencies

plugins {
    id(AppPlugins.androidApplication)
    id(AppPlugins.androidKotlin)
    id(AppPlugins.kotlinKapt)
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.Sdk.compile

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.Sdk.min
        targetSdk = AppConfig.Sdk.target
        versionCode = AppConfig.Version.code
        versionName = AppConfig.Version.name

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

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
        ":common:utils",
        ":core:design",
        ":core:navigation:api",
        ":core:navigation:compose-impl",
        ":core:coroutines:dispatchers",
        ":core:coroutines:scopes",
        ":core:data:db",
        ":core:data:http-client",
        ":features:onboarding:api",
        ":features:onboarding:compose-impl",
        ":features:login:api",
        ":features:login:compose-impl",
    )

    dependency(AppDependencies.immutableCollections)
    dependency(AppDependencies.Ktx.core)
    dependency(AppDependencies.Coroutines.core)
    dependency(AppDependencies.Coroutines.android)
    dependency(AppDependencies.Scout.core)
    dependency(AppDependencies.Accompanist.permissions)
    dependency(AppDependencies.Orbit.viewModel)
    dependency(AppDependencies.Orbit.compose)
    dependency(AppDependencies.Coil.compose)
    dependency(AppDependencies.Coil.gif)
    dependency(AppDependencies.Compose.bom)
    dependency(AppDependencies.Compose.material)
    dependency(AppDependencies.Compose.lifeCycleRuntime)
    dependency(AppDependencies.Compose.activity)
    dependency(AppDependencies.Compose.navigation)
    dependency(AppDependencies.Compose.preview)
    dependency(AppDependencies.Compose.debugPreview)
    dependency(AppDependencies.Splash.core)
    dependency(AppDependencies.Testing.junit)
    dependency(AppDependencies.Scout.validator)
    dependency(AppDependencies.Scout.graphCollector)
}