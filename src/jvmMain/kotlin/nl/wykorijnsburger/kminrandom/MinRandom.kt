package nl.wykorijnsburger.kminrandom

import nl.wykorijnsburger.kminrandom.exception.NoConstructorException
import nl.wykorijnsburger.kminrandom.exception.PrivateConstructorException
import nl.wykorijnsburger.kminrandom.exception.SelfReferentialException
import nl.wykorijnsburger.kminrandom.exception.UnsupportedClassException
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

public object KMinRandom {
    /**
     * Supplies a value that KMinRandom will use to generate a value for the supplied [KClass]
     * This value will be returned as the value for the supplied [KClass] whenever [minRandom] is called
     *
     * Note that KMinRandom is stateful. Supplied values from other tests could, based on the run order of the tests, be used in other tests
     */
    public fun <T : Any> supplyValueForClass(clazz: KClass<T>, value: T) {
        if (!clazz.isInstance(value)) throw RuntimeException()

        classToMinRandom[clazz] = { value }
    }

    /**
     * Used for internal tests
     */
    internal fun <T : Any> removeSupportForClass(clazz: KClass<T>) {
        val standardGenerateFunction = standardClassToMinRandom[clazz]
        if (standardGenerateFunction != null) {
            classToMinRandom.replace(clazz, standardGenerateFunction)
        } else {
            classToMinRandom.remove(clazz)
        }
    }
}

/**
 * Generates a minimal random instance of the supplied KClass.
 *
 * A new value is generated on each invocation. If it is fine to reuse values, consider using [minRandomCached].
 */
public fun <T : Any> KClass<T>.minRandom(): T = generateMinRandom(this)

/**
 * Generates a minimal random instance of the supplied KClass.
 *
 * A new value is generated on each invocation. If it is fine to reuse values, consider using [minRandomCached].
 *
 * Experimental syntax using the reified type added in Kotlin 1.3.40.
 * See [Kotlin 1.3.40 release notes (Accessing the reified type using reflection on JVM)](https://blog.jetbrains.com/kotlin/2019/06/kotlin-1-3-40-released/) for more details.
 */
@OptIn(ExperimentalStdlibApi::class)
public inline fun <reified T> minRandom(): T {
    return typeOf<T>().jvmErasure.minRandom() as T
}

/**
 * Generates a minimal random instance of the supplied KClass
 */
public fun <T : Any> generateMinRandom(clazz: KClass<T>): T {

    val objectInstance = clazz.objectInstance

    // Supported types can be directly returned without inspecting constructor
    when {
        objectInstance != null -> return objectInstance
        clazz.sealedSubclasses.isNotEmpty() -> return sealedClassMinRandom(clazz)
        classToMinRandom.containsKey(clazz) -> return clazz.randomInstance() as T
        clazz.java.isEnum -> return clazz.randomEnum()
    }

    clazz.checkForUnsupportedTypes(mutableSetOf())

    val constructor = clazz.getConstructorWithTheLeastArguments() ?: throw NoConstructorException()

    if (constructor.visibility == KVisibility.PRIVATE) throw PrivateConstructorException()

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
    return (classToMinRandom[this]?.invoke() ?: this.starProjectedType.jvmErasure.minRandom()) as T
}

private fun <T : Any> KClass<T>.checkForUnsupportedTypes(checkedTypes: MutableSet<KClass<*>>) {
    if (objectInstance != null) return
    if (classToMinRandom.containsKey(this)) return

    val constructor = getConstructorWithTheLeastArguments()

    if (checkedTypes.contains(this)) throw SelfReferentialException()
    if (constructor == null) throw UnsupportedClassException(this)

    constructor.parameters
        .filter { !it.isOptional && !it.type.isMarkedNullable }
        .map { it.type.jvmErasure }
        .forEach {
            checkedTypes.add(this)
            val checkedTypesCopy = checkedTypes.toMutableSet()
            it.checkForUnsupportedTypes(checkedTypesCopy)
        }
}

private fun <T : Any> KClass<T>.getConstructorWithTheLeastArguments(): KFunction<T>? {
    val primaryConstructor = primaryConstructor

    if (primaryConstructor != null) return primaryConstructor

    return constructors.minByOrNull { it.parameters.size }
}

private fun <T : Any> sealedClassMinRandom(clazz: KClass<T>) =
    clazz.sealedSubclasses[Random.nextInt(clazz.sealedSubclasses.size)].minRandom()
