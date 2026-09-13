package nl.wykorijnsburger.kminrandom

import nl.wykorijnsburger.kminrandom.exception.NoConstructorException
import nl.wykorijnsburger.kminrandom.exception.PrivateConstructorException
import nl.wykorijnsburger.kminrandom.exception.SelfReferentialException
import nl.wykorijnsburger.kminrandom.exception.SuppliedValueException
import nl.wykorijnsburger.kminrandom.exception.UnsupportedClassException
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

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

        // No need to clear checkedClasses: supplying a value can only make more classes supported.
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
        checkedClasses.clear()
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
@Suppress("UNCHECKED_CAST")
private fun <T : Any> generateMinRandom(clazz: KClass<T>, checkTypes: Boolean): T {
    // Supported types can be directly returned without inspecting constructor
    when (val kind = clazz.kind()) {
        is ClassKind.ObjectInstance -> return kind.instance as T

        is ClassKind.Sealed -> return kind.subclasses[Random.nextInt(kind.subclasses.size)].minRandom() as T

        is ClassKind.Regular -> {
            val supplier = classToMinRandom[clazz]
            if (supplier != null) return supplier() as T
            if (kind.isEnum) return clazz.randomEnum()
        }
    }

    if (checkTypes) clazz.checkForUnsupportedTypes(mutableSetOf())

    val plan = clazz.constructorPlan()
    val constructor = plan.constructor ?: throw NoConstructorException()

    if (plan.isPrivate) throw PrivateConstructorException()

    val parameterMap: MutableMap<KParameter, Any?> = mutableMapOf()
    plan.requiredParameters.forEach {
        parameterMap[it.parameter] = when {
            it.isNullable -> null
            it.isEnum -> it.erasure.randomEnum()
            else -> it.randomInstance()
        }
    }

    return constructor.callBy(parameterMap)
}

private fun ParameterPlan.randomInstance(): Any? {
    val classifier = classifier ?: return null
    return classToMinRandom[classifier]?.invoke() ?: generateMinRandom(generatedClass, checkTypes = false)
}

/**
 * [path] holds the classes from the outermost class down to, but excluding, this class.
 * Encountering a class that is already on the path means the class references itself.
 */
private fun KClass<*>.checkForUnsupportedTypes(path: MutableSet<KClass<*>>) {
    if (this in checkedClasses) return
    if (kind() is ClassKind.ObjectInstance) return
    if (classToMinRandom.containsKey(this)) return

    val plan = constructorPlan()

    if (this in path) throw SelfReferentialException()
    if (plan.constructor == null) throw UnsupportedClassException(this)

    path.add(this)
    plan.requiredParameters
        .filter { !it.isNullable }
        .forEach { it.erasure.checkForUnsupportedTypes(path) }
    path.remove(this)

    // The whole subtree passed. A later cycle through this class would have been caught here, so this is safe to reuse.
    checkedClasses.add(this)
}
