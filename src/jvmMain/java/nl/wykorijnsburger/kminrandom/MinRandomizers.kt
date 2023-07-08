package nl.wykorijnsburger.kminrandom

import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.net.URL
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.Date
import java.util.LinkedList
import java.util.Optional
import java.util.Queue
import java.util.UUID
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

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
    Queue::class to { LinkedList<Any>() },

    // Java.Time
    Instant::class to { Instant.now() },
    LocalDate::class to { LocalDate.now() },
    LocalTime::class to { LocalTime.now() },
    ZonedDateTime::class to { ZonedDateTime.now() },

    // Java.Math
    BigDecimal::class to { BigDecimal.valueOf(Random.nextDouble()) },
    BigInteger::class to { BigInteger(5, random) },

    // Java.Util
    Date::class to { Date.from(Instant.now()) },
    Optional::class to { Optional.empty<Any>() },
    UUID::class to { UUID.randomUUID() },

    // Java.Net
    URI::class to { randomURI() },
    URL::class to { randomURI().toURL() }
)

internal val classToMinRandom = standardClassToMinRandom.toMutableMap()

internal fun <T : Any> KClass<T>.randomEnum(): T {
    val enumConstants = java.enumConstants

    return if (enumConstants == null || enumConstants.isEmpty()) {
        throw RuntimeException("Cannot generate random value for empty enum ${this.simpleName}")
    } else {
        enumConstants[Random.nextInt(enumConstants.size)]
    }
}

internal fun KType.randomEnum(): Any {
    return this.jvmErasure.randomEnum()
}
