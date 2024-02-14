import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project
import dependencies.Dependency
import dependencies.ImplementationType
import dependencies.notationWithVersion

fun DependencyHandler.dependency(
    dependency: Dependency,
    forcedImplementationType: ImplementationType? = null,
    forcedPlatform: Boolean? = null
) {
    add(
        (forcedImplementationType ?: dependency.implementationType).configurationName,
        if (forcedPlatform ?: dependency.isPlatform) {
            platform(dependency.notationWithVersion)
        } else {
            dependency.notationWithVersion
        },
    )
}

fun DependencyHandler.modules(vararg names: String) {
    names.forEach { name ->
        add("implementation", project(name))
    }
}