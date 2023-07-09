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
import java.util.Optional
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

internal actual fun platformSpecificClassToMinRandom() = mapOf<KClass<*>, () -> Any>(
    // Java.Time
    Instant::class to { Instant.now() },
    LocalDate::class to { LocalDate.now() },
    LocalTime::class to { LocalTime.now() },
    ZonedDateTime::class to { ZonedDateTime.now() },

    // Java.Math
    BigDecimal::class to { BigDecimal.valueOf(random.nextDouble()) },
    BigInteger::class to { BigInteger(5, random) },

    // Java.Util
    Date::class to { Date.from(Instant.now()) },
    Optional::class to { Optional.empty<Any>() },
    UUID::class to { UUID.randomUUID() },

    // Java.Net
    URI::class to { randomURI() },
    URL::class to { randomURI().toURL() },
)

internal actual fun <T : Any> KClass<T>.randomEnum(): T? {
    if (!java.isEnum) return null

    val enumConstants = java.enumConstants

    return if (enumConstants == null || enumConstants.isEmpty()) {
        throw RuntimeException("Cannot generate random value for empty enum ${this.simpleName}")
    } else {
        enumConstants[random.nextInt(enumConstants.size)]
    }
}

/**
 * Returns a random Enum if the provided [KType] is an enum.
 */
internal actual fun KType.randomEnum(): Any? {
    return if (jvmErasure.java.isEnum) {
        this.jvmErasure.randomEnum()
    } else {
        null
    }
}
