package nl.wykorijnsburger.kminrandom

import nl.wykorijnsburger.kminrandom.exception.NoConstructorException
import nl.wykorijnsburger.kminrandom.exception.PrivateConstructorException
import kotlin.random.Random
import kotlin.reflect.*
import kotlin.reflect.full.primaryConstructor

/**
 * Generates a minimal random instance of the supplied KClass.
 *
 * A new value is generated on each invocation. If it is fine to reuse values, consider using [minRandomCached].
 *
 * Experimental syntax using the reified type added in Kotlin 1.3.40.
 * See [Kotlin 1.3.40 release notes (Accessing the reified type using reflection on JVM)](https://blog.jetbrains.com/kotlin/2019/06/kotlin-1-3-40-released/) for more details.
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> minRandomMP(): T {
    val type = typeOf<T>()

   return minRandomMP(type) as T
}

@OptIn(ExperimentalStdlibApi::class)
fun <T> minRandomMP(type: KType): T {
    val minRandom = typeToMinRandom[type]
    if (minRandom != null ) return minRandom() as T

    val constructor = (type.classifier as KClass<Any>).getConstructorWithTheLeastArguments() ?: throw NoConstructorException()

    if (constructor.visibility == KVisibility.PRIVATE) throw PrivateConstructorException()

    val parameters = constructor.parameters

    val parameterMap: MutableMap<KParameter, Any?> = mutableMapOf()
    parameters
        .filter { !it.isOptional }
        .forEach {
            val paramType = it.type

            val randomParameter = when {
                paramType.isMarkedNullable -> null
//                type.jvmErasure.java.isEnum -> type.randomEnum()
                else -> paramType.randomInstance<Any>()
            }

            parameterMap[it] = randomParameter
        }

    return constructor.callBy(parameterMap) as T
}

@ExperimentalStdlibApi
internal val typeToMinRandom = mapOf<KType, () -> Any>(
    // Numbers
    typeOf<Double>() to { Random.nextDouble() },
    typeOf<Float>() to { Random.nextFloat() },
    typeOf<Long>() to { Random.nextLong() },
    typeOf<Int>() to { Random.nextInt() },
    typeOf<Short>() to { Random.nextInt().toShort() },
    typeOf<Byte>() to { Random.nextInt().toByte() }
)


@OptIn(ExperimentalStdlibApi::class)
@Suppress("UNCHECKED_CAST")
private fun <T: Any> KType.randomInstance(): T {
    val minRandom = typeToMinRandom[this]
    if (minRandom != null ) return minRandom() as T

    return minRandomMP(this)
}

private fun <T : Any> KClass<T>.getConstructorWithTheLeastArguments(): KFunction<T>? {
    val primaryConstructor = primaryConstructor

    if (primaryConstructor != null) return primaryConstructor

    return constructors.minBy { it.parameters.size }
}
