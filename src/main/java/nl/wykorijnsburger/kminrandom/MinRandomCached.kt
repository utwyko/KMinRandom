package nl.wykorijnsburger.kminrandom

import kotlin.reflect.KClass

/**
 * Generates a minimal random instance of the supplied KClass and stores it in memory.
 * Subsequent calls to [minRandomCached] with the same type will return an identical result.
 *
 * Since already generated values can be reused, there is a performance gain from using this
 * method compared to [minRandom].
 */
public fun <T : Any> KClass<T>.minRandomCached(): T = generateMinRandomCached(this)

/**
 * Generates a minimal random instance of the supplied KClass and stores it in memory.
 * Subsequent calls to [minRandomCached] with the same type will return an identical result.
 *
 * Since already generated values can be reused, there is a performance gain from using this method
 * compared to [minRandom].
 */
public inline fun <reified T : Any> minRandomCached(): T = T::class.minRandomCached()

/**
 * Generates a minimal random instance of the supplied KClass
 */
public fun <T : Any> generateMinRandomCached(clazz: KClass<T>): T {
    val qualifiedClassName = clazz.qualifiedName

    @Suppress("UNCHECKED_CAST")
    val cachedInstance = minRandomCache[qualifiedClassName] as T?

    if (cachedInstance == null) {
        val generatedMinRandom = generateMinRandom(clazz)

        if (qualifiedClassName != null) {
            minRandomCache[qualifiedClassName] = generatedMinRandom
        }
        return generatedMinRandom
    }

    return cachedInstance
}

private val minRandomCache = mutableMapOf<String, Any>()
