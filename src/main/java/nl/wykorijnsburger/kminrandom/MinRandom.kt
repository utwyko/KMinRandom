package nl.wykorijnsburger.kminrandom

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> KClass<T>.minRandom() = generateMinRandom(this)

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> generateMinRandom(clazz: KClass<T>): T {
    // Supported types can be directly returned without inspecting constructor
    when {
        classToMinRandom.containsKey(clazz) -> return clazz.randomInstance() as T
        clazz.java.isEnum -> return clazz.randomEnum()
    }

    clazz.checkForUnsupportedTypes()

    val constructor = clazz.getConstructorWithTheLeastArguments()!!
    val parameters = constructor.parameters

    val parameterMap: MutableMap<KParameter, Any?> = mutableMapOf()
    parameters
        .filter { !it.isOptional }
        .forEach {
            val type = it.type

            val randomParameter = when {
                type.isMarkedNullable -> null
                type.randomEnum() != null -> type.randomEnum()
                else -> type.classifier?.randomInstance()
            }

            parameterMap[it] = randomParameter
        }

    return constructor.callBy(parameterMap)
}

private fun KClassifier.randomInstance(): Any {
    return classToMinRandom[this] ?: this.starProjectedType.jvmErasure.minRandom()
}

private fun <T : Any> KClass<T>.checkForUnsupportedTypes() {
    if (classToMinRandom.containsKey(this)) {
        return
    }

    val constructor = getConstructorWithTheLeastArguments()

    if (constructor == null) {
        throw RuntimeException("Could not generate random instance of $this")
    } else {
        constructor.parameters
            .filter { !it.isOptional && !it.type.isMarkedNullable }
            .map { it.type.jvmErasure }
            .forEach { it.checkForUnsupportedTypes() }
    }
}

private fun <T : Any> KClass<T>.getConstructorWithTheLeastArguments(): KFunction<T>? {
    val primaryConstructor = primaryConstructor

    if (primaryConstructor != null) return primaryConstructor

    return constructors.sortedBy { it.parameters.size }
        .firstOrNull()
}