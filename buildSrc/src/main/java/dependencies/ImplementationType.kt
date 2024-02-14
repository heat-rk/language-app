package dependencies

enum class ImplementationType(val configurationName: String) {
    DEFAULT("implementation"),
    API("api"),
    KAPT("kapt"),
    DEBUG("debugImplementation"),
    TEST("testImplementation"),
    ANDROID_TEST("androidTestImplementation");
}