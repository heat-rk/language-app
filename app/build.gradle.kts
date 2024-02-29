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
    modules(
        ":common:utils",
        ":core:design",
        ":core:navigation:api",
        ":core:navigation:compose-impl",
        ":core:coroutines:dispatchers",
        ":core:coroutines:scopes",
        ":core:data:db",
        ":core:data:http-client",
        ":core:data:supabase",
        ":features:onboarding:api",
        ":features:onboarding:compose-impl",
        ":features:auth:api",
        ":features:auth:compose-impl",
        ":features:main:api",
        ":features:main:compose-impl",
    )

    dependencies(
        AppDependencies.immutableCollections,
        AppDependencies.Ktx.core,
        AppDependencies.Coroutines.core,
        AppDependencies.Coroutines.android,
        AppDependencies.Scout.core,
        AppDependencies.Orbit.viewModel,
        AppDependencies.Orbit.compose,
        AppDependencies.Coil.compose,
        AppDependencies.Coil.gif,
        AppDependencies.Compose.bom,
        AppDependencies.Compose.material,
        AppDependencies.Compose.lifeCycleRuntime,
        AppDependencies.Compose.activity,
        AppDependencies.Compose.navigation,
        AppDependencies.Compose.preview,
        AppDependencies.Splash.core,
    )

    debugDependencies(
        AppDependencies.Compose.debugPreview,
    )

    testDependencies(
        AppDependencies.Testing.junit,
        AppDependencies.Scout.validator,
        AppDependencies.Scout.graphCollector,
    )
}