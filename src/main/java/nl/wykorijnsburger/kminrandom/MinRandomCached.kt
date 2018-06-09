package nl.wykorijnsburger.kminrandom

import kotlin.reflect.KClass


/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> KClass<T>.minRandomCached() = generateMinRandomCached(this)

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> generateMinRandomCached(clazz: KClass<T>): T {
    val qualifiedClassName = clazz.qualifiedName
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