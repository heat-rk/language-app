import dependencies.AppDependencies

plugins {
    id(AppPlugins.javaLibrary)
    id(AppPlugins.jvmKotlin)
}

dependencies {
    dependencies(
        AppDependencies.kotlinXSerialization,
        AppDependencies.Scout.core,
    )
}