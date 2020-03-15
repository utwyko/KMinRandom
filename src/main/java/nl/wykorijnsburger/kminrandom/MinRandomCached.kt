package nl.wykorijnsburger.kminrandom

import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> KClass<T>.minRandomCached() = generateMinRandomCached(this)

@UseExperimental(ExperimentalStdlibApi::class)
inline fun <reified T> minRandomCached(): T {
    val clazz = typeOf<T>()
    return generateMinRandomCached(clazz.jvmErasure) as T
}

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> generateMinRandomCached(clazz: KClass<T>): T {
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
