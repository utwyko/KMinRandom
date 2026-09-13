package nl.wykorijnsburger.kminrandom.benchmark

import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.Date
import java.util.Optional
import java.util.UUID
import kotlin.reflect.KClass

data class FlatDC(
    val int: Int,
    val long: Long,
    val double: Double,
    val boolean: Boolean,
    val string: String,
    val char: Char,
    val float: Float,
    val short: Short,
)

data class WideDC(
    val int: Int,
    val long: Long,
    val double: Double,
    val float: Float,
    val short: Short,
    val byte: Byte,
    val char: Char,
    val string1: String,
    val string2: String,
    val string3: String,
    val boolean: Boolean,
    val intArray: IntArray,
    val list: List<String>,
    val set: Set<Int>,
    val map: Map<String, Int>,
    val sequence: Sequence<String>,
    val instant: Instant,
    val localDate: LocalDate,
    val localTime: LocalTime,
    val zonedDateTime: ZonedDateTime,
    val bigDecimal: BigDecimal,
    val bigInteger: BigInteger,
    val date: Date,
    val optional: Optional<String>,
    val uuid: UUID,
    val uri: URI,
    val nullableString: String?,
    val nullableInt: Int?,
    val color: Color,
    val shape: Shape,
)

data class DefaultsDC(
    val required1: Int,
    val required2: String,
    val required3: Long,
    val required4: Boolean,
    val optional1: Int = 1,
    val optional2: String = "default",
    val optional3: Long = 3L,
    val optional4: Boolean = true,
)

enum class Color { RED, GREEN, BLUE, YELLOW }

sealed class Shape {
    data class Circle(val radius: Double) : Shape()
    data class Rectangle(val width: Double, val height: Double) : Shape()
    data object Point : Shape()
}

object Singleton

// Nested models with fan-out 2: NestedN holds two NestedN-1 values, so NestedN has 2^(N-1) leaves.
data class Nested1(val int: Int, val string: String)
data class Nested2(val left: Nested1, val right: Nested1)
data class Nested3(val left: Nested2, val right: Nested2)
data class Nested4(val left: Nested3, val right: Nested3)
data class Nested5(val left: Nested4, val right: Nested4)
data class Nested6(val left: Nested5, val right: Nested5)
data class Nested7(val left: Nested6, val right: Nested6)
data class Nested8(val left: Nested7, val right: Nested7)

val nestedByDepth: Map<Int, KClass<*>> = mapOf(
    1 to Nested1::class,
    2 to Nested2::class,
    3 to Nested3::class,
    4 to Nested4::class,
    5 to Nested5::class,
    6 to Nested6::class,
    7 to Nested7::class,
    8 to Nested8::class,
)
