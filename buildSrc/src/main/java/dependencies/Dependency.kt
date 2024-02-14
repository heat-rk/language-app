package dependencies

data class Dependency(
    val notation: String,
    val implementationType: ImplementationType,
    val isPlatform: Boolean,
    val version: String? = null,
)
