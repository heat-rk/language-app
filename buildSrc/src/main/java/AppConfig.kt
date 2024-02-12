object AppConfig {
    const val applicationId = "ru.heatrk.languageapp"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val jvmTarget = "1.8"
    const val composeKotlinCompilerExtensionVersion = "1.5.9"

    val buildConfigFields = emptyArray<BuildConfigField>()

    object Sdk {
        const val compile = 34
        const val min = 30
        const val target = 34
    }

    object Version {
        const val code = 1
        const val name = "1.0"
    }
}