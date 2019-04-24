package nl.wykorijnsburger.kminrandom

import kotlin.random.Random
import kotlin.reflect.*
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

object KMinRandom {
    /**
     * Supplies a value that KMinRandom will use to generate a value for the supplied [KClass]
     * This value will be returned as the value for the supplied [KClass] whenever [minRandom] is called
     *
     * Note that KMinRandom is stateful. Supplied values from other tests could, based on the run order of the tests, be used in other tests
     */
    fun <T : Any> supplyValueForClass(clazz: KClass<T>, value: T) {
        if (!clazz.isInstance(value)) throw RuntimeException()

        classToMinRandom[clazz] = value
    }

    /**
     * Used for internal tests
     */
    internal fun <T : Any> removeSupportForClass(clazz: KClass<T>) {
        classToMinRandom.remove(clazz)
    }
}

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> KClass<T>.minRandom() = generateMinRandom(this)

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> generateMinRandom(clazz: KClass<T>): T {

    val objectInstance = clazz.objectInstance

    // Supported types can be directly returned without inspecting constructor
    when {
        objectInstance != null -> return objectInstance
        clazz.sealedSubclasses.isNotEmpty() -> return sealedClassMinRandom(clazz)
        classToMinRandom.containsKey(clazz) -> return clazz.randomInstance() as T
        clazz.java.isEnum -> return clazz.randomEnum()
    }

    clazz.checkForUnsupportedTypes()

    val constructor = clazz.getConstructorWithTheLeastArguments() ?: throw noConstructorException

    if (constructor.visibility == KVisibility.PRIVATE) throw privateConstructorException

    val parameters = constructor.parameters

    val parameterMap: MutableMap<KParameter, Any?> = mutableMapOf()
    parameters
        .filter { !it.isOptional }
        .forEach {
            val type = it.type

            val randomParameter = when {
                type.isMarkedNullable -> null
                type.jvmErasure.java.isEnum -> type.randomEnum()
                else -> type.classifier?.randomInstance()
            }

            parameterMap[it] = randomParameter
        }

    return constructor.callBy(parameterMap)
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any> KClassifier.randomInstance(): T {
    return (classToMinRandom[this] ?: this.starProjectedType.jvmErasure.minRandom()) as T
}

private fun <T : Any> KClass<T>.checkForUnsupportedTypes() {
    if (objectInstance != null) return

    if (classToMinRandom.containsKey(this)) return

    val constructor = getConstructorWithTheLeastArguments()

    if (constructor == null) {
        throw RuntimeException("Could not generate random instance of $this. You can supply your own instance of this class by using KMinRandom.supplyValueForClass().")
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

private fun <T : Any> sealedClassMinRandom(clazz: KClass<T>) =
    clazz.sealedSubclasses[Random.nextInt(clazz.sealedSubclasses.size)].minRandom()
