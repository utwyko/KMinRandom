package nl.wykorijnsburger.kminrandom

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal expect fun platformSpecificClassToMinRandom(): Map<KClass<*>, () -> Any>

internal val standardClassToMinRandom = mapOf<KClass<*>, () -> Any>(
    // Numbers
    Double::class to { Random.nextDouble() },
    Float::class to { Random.nextFloat() },
    Long::class to { Random.nextLong() },
    Int::class to { Random.nextInt() },
    Short::class to { Random.nextInt().toShort() },
    Byte::class to { Random.nextInt().toByte() },

    Char::class to { randomString().first() },
    String::class to { randomString() },
    Boolean::class to { Random.nextBoolean() },

    // Arrays
    DoubleArray::class to { doubleArrayOf() },
    FloatArray::class to { floatArrayOf() },
    LongArray::class to { longArrayOf() },
    IntArray::class to { intArrayOf() },
    ShortArray::class to { shortArrayOf() },
    ByteArray::class to { byteArrayOf() },
    CharArray::class to { charArrayOf() },
    BooleanArray::class to { booleanArrayOf() },

    // Collections
    List::class to { emptyList<Any>() },
    Set::class to { emptySet<Any>() },
    Iterable::class to { emptyList<Any>() },
    Sequence::class to { emptySequence<Any>() },
    Map::class to { emptyMap<Any, Any>() },
) + platformSpecificClassToMinRandom()

internal val classToMinRandom = standardClassToMinRandom.toMutableMap()

internal expect fun KType.randomEnum(): Any?

internal expect fun <T : Any> KClass<T>.randomEnum(): T?
