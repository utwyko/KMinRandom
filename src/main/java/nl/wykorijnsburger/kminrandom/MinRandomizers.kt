package nl.wykorijnsburger.kminrandom

import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure
import kotlin.streams.asSequence


private val random = ThreadLocalRandom.current()

internal val classToMinRandom = mapOf(
    // Numbers
    Double::class to random.nextDouble(),
    Float::class to random.nextFloat(),
    Long::class to random.nextLong(),
    Int::class to random.nextInt(),
    Short::class to random.nextInt().toShort(),
    Byte::class to random.nextInt().toByte(),

    Char::class to randomString().first(),
    String::class to randomString(),
    Boolean::class to random.nextBoolean(),

    // Arrays
    DoubleArray::class to doubleArrayOf(),
    FloatArray::class to floatArrayOf(),
    LongArray::class to longArrayOf(),
    IntArray::class to intArrayOf(),
    ShortArray::class to shortArrayOf(),
    ByteArray::class to byteArrayOf(),
    CharArray::class to charArrayOf(),
    BooleanArray::class to booleanArrayOf(),

    // Collections
    List::class to emptyList<Any>(),
    Set::class to emptySet<Any>(),
    Iterable::class to emptyList<Any>(),
    Sequence::class to emptySequence<Any>(),
    Map::class to emptyMap<Any, Any>(),
    Queue::class to LinkedList<Any>(),

    // Java.Time
    Instant::class to Instant.now(),
    LocalDate::class to LocalDate.now(),
    LocalTime::class to LocalTime.now(),
    ZonedDateTime::class to ZonedDateTime.now(),

    // Java.Math
    BigDecimal::class to BigDecimal.valueOf(random.nextDouble()),
    BigInteger::class to BigInteger(5, random),

    // Java.Util
    Date::class to Date.from(Instant.now()),
    Optional::class to Optional.empty<Any>(),
    UUID::class to UUID.randomUUID()
)

private fun randomString(): String {
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    return random.ints(random.nextInt(1, 50).toLong(), 0, source.length)
        .asSequence()
        .map(source::get)
        .joinToString("")
}

internal fun KType.randomEnum(): Any? {
    val enumConstants = this.jvmErasure.java.enumConstants

    return if (enumConstants == null || enumConstants.isEmpty()) {
        null
    } else {
        enumConstants[random.nextInt(enumConstants.size)]
    }
}