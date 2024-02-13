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
    // modules
    implementation(project(":common:utils"))
    implementation(project(":core:design"))
    implementation(project(":core:navigation:api"))
    implementation(project(":core:navigation:compose-impl"))
    implementation(project(":core:coroutines:dispatchers"))
    implementation(project(":core:coroutines:scopes"))
    implementation(project(":core:data:db"))
    implementation(project(":core:data:http-client"))

    implementation(project(":features:onboarding:api"))
    implementation(project(":features:onboarding:compose-impl"))

    // dependencies
    platformImplementation(AppDependencies.Compose.platformImplementations)

    implementation(AppDependencies.immutableCollections)
    implementation(AppDependencies.Ktx.allImplementations)

    implementation(AppDependencies.Coroutines.allImplementations)
    implementation(AppDependencies.Scout.allImplementations)
    implementation(AppDependencies.Accompanist.allImplementations)
    implementation(AppDependencies.Orbit.allImplementations)
    implementation(AppDependencies.Coil.allImplementations)
    implementation(AppDependencies.Compose.material)
    implementation(AppDependencies.Compose.lifeCycleRuntime)
    implementation(AppDependencies.Compose.activity)
    implementation(AppDependencies.Compose.navigation)
    implementation(AppDependencies.Compose.preview)
    implementation(AppDependencies.Splash.allImplementations)

    debugImplementation(AppDependencies.Compose.debugImplementations)
}