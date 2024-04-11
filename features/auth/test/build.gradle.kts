import dependencies.AppDependencies

plugins {
    id(AppPlugins.javaLibrary)
    id(AppPlugins.jvmKotlin)
}

dependencies {
    modules(
        ":features:auth:api",
        ":features:auth:compose-impl",
    )

    dependencies(
        AppDependencies.Coroutines.core,
    )
}