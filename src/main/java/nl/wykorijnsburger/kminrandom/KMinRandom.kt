package nl.wykorijnsburger.kminrandom

import nl.wykorijnsburger.kminrandom.exception.NoConstructorException
import nl.wykorijnsburger.kminrandom.exception.PrivateConstructorException
import nl.wykorijnsburger.kminrandom.exception.SelfReferentialException
import nl.wykorijnsburger.kminrandom.exception.SuppliedValueException
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

public object KMinRandom {
    /**
     * Supplies a value that KMinRandom will use to generate a value for the supplied [KClass]
     * This value will be returned as the value for the supplied [KClass] whenever [minRandom] is called
     *
     * Note that KMinRandom is stateful. Supplied values from other tests could be used in other tests
     * based on the run order of the tests.
     */
    public fun <T : Any> supplyValueForClass(clazz: KClass<T>, value: T) {
        if (!clazz.isInstance(value)) throw SuppliedValueException(clazz)

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
 */
public inline fun <reified T : Any> minRandom(): T = T::class.minRandom()

/**
 * Generates a minimal random instance of the supplied KClass
 */
public fun <T : Any> generateMinRandom(clazz: KClass<T>): T = generateMinRandom(clazz, checkTypes = true)

/**
 * When [checkTypes] is true, [clazz] and all classes nested in its constructor parameters are checked for
 * unsupported and self-referential types before anything is generated. Nested parameter values are generated
 * with [checkTypes] set to false, as the check on the outermost class already covered them.
 */
private fun <T : Any> generateMinRandom(clazz: KClass<T>, checkTypes: Boolean): T {
    val objectInstance = clazz.objectInstance

    // Supported types can be directly returned without inspecting constructor
    when {
        objectInstance != null -> return objectInstance
        clazz.sealedSubclasses.isNotEmpty() -> return sealedClassMinRandom(clazz)
        classToMinRandom.containsKey(clazz) -> return clazz.randomInstance() as T
        clazz.java.isEnum -> return clazz.randomEnum()
    }

    if (checkTypes) clazz.checkForUnsupportedTypes(mutableSetOf())

    val constructor = clazz.getConstructorWithTheLeastArguments() ?: throw NoConstructorException()

    if (constructor.visibility == KVisibility.PRIVATE) throw PrivateConstructorException()

    val parameters = constructor.parameters

    // callBy lets the constructor fill in default values, but is slower than call. Only use it when needed.
    if (parameters.none { it.isOptional }) {
        return constructor.call(*Array(parameters.size) { parameters[it].randomValue() })
    }

    val parameterMap: MutableMap<KParameter, Any?> = mutableMapOf()
    parameters
        .filter { !it.isOptional }
        .forEach { parameterMap[it] = it.randomValue() }

    return constructor.callBy(parameterMap)
}

private fun KParameter.randomValue(): Any? {
    val type = type

    return when {
        type.isMarkedNullable -> null
        type.jvmErasure.java.isEnum -> type.randomEnum()
        else -> type.classifier?.randomInstance()
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any> KClassifier.randomInstance(): T =
    (classToMinRandom[this]?.invoke() ?: generateMinRandom(starProjectedType.jvmErasure, checkTypes = false)) as T

/**
 * [path] holds the classes from the outermost class down to, but excluding, this class.
 * Encountering a class that is already on the path means the class references itself.
 */
private fun KClass<*>.checkForUnsupportedTypes(path: MutableSet<KClass<*>>) {
    if (objectInstance != null) return
    if (classToMinRandom.containsKey(this)) return

    val constructor = getConstructorWithTheLeastArguments()

    if (this in path) throw SelfReferentialException()
    if (constructor == null) throw UnsupportedClassException(this)

    path.add(this)
    constructor.parameters
        .filter { !it.isOptional && !it.type.isMarkedNullable }
        .forEach { it.type.jvmErasure.checkForUnsupportedTypes(path) }
    path.remove(this)
}

private fun <T : Any> KClass<T>.getConstructorWithTheLeastArguments(): KFunction<T>? {
    val primaryConstructor = primaryConstructor

    if (primaryConstructor != null) return primaryConstructor

    return constructors.minByOrNull { it.parameters.size }
}

private fun <T : Any> sealedClassMinRandom(clazz: KClass<T>) =
    clazz.sealedSubclasses[Random.nextInt(clazz.sealedSubclasses.size)].minRandom()
