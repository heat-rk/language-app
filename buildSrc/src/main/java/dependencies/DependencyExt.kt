package dependencies

val Dependency.notationWithVersion
    get() = buildString {
        append(notation)

        if (version != null) {
            append(":")
            append(version)
        }
    }

fun defaultDependency(
    notation: String,
    version: String? = null,
    isPlatform: Boolean = false
) = Dependency(
    notation = notation,
    version = version,
    implementationType = ImplementationType.DEFAULT,
    isPlatform = isPlatform,
)

fun apiDependency(
    notation: String,
    version: String? = null,
    isPlatform: Boolean = false
) = Dependency(
    notation = notation,
    version = version,
    implementationType = ImplementationType.API,
    isPlatform = isPlatform,
)

fun kaptDependency(
    notation: String,
    version: String? = null,
    isPlatform: Boolean = false
) = Dependency(
    notation = notation,
    version = version,
    implementationType = ImplementationType.KAPT,
    isPlatform = isPlatform,
)

fun debugDependency(
    notation: String,
    version: String? = null,
    isPlatform: Boolean = false
) = Dependency(
    notation = notation,
    version = version,
    implementationType = ImplementationType.DEBUG,
    isPlatform = isPlatform,
)

fun testDependency(
    notation: String,
    version: String? = null,
    isPlatform: Boolean = false
) = Dependency(
    notation = notation,
    version = version,
    implementationType = ImplementationType.TEST,
    isPlatform = isPlatform,
)

fun androidTestDependency(
    notation: String,
    version: String? = null,
    isPlatform: Boolean = false
) = Dependency(
    notation = notation,
    version = version,
    implementationType = ImplementationType.ANDROID_TEST,
    isPlatform = isPlatform,
)

