package nl.wykorijnsburger.kminrandom

import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

/**
 * Generates a minimal random instance of the supplied KClass
 */
fun <T : Any> KClass<T>.minRandomCached() = generateMinRandomCached(this)

/**
 * Experimental syntax using the reified type added in Kotlin 1.3.40.
 * See [Kotlin 1.3.40 release notes (Accessing the reified type using reflection on JVM)](https://blog.jetbrains.com/kotlin/2019/06/kotlin-1-3-40-released/) for more details.
 */
@OptIn(ExperimentalStdlibApi::class)
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
