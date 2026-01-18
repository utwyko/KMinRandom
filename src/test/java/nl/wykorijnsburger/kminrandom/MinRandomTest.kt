package nl.wykorijnsburger.kminrandom

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import java.sql.SQLData
import kotlin.test.Test

internal class MinRandomTest {
    data class BasicTypesDC(
        val double: Double,
        val float: Float,
        val long: Long,
        val int: Int,
        val short: Short,
        val byte: Byte,
        val char: Char,
        val boolean: Boolean,
        val string: String,
    )

    @Test
    fun `Should generate random values for Kotlin Basic Types`() {
        val randomDC = BasicTypesDC::class.minRandom()

        assertThat(randomDC.double).isNotNull()
        assertThat(randomDC.float).isNotNull()
        assertThat(randomDC.long).isNotNull()
        assertThat(randomDC.int).isNotNull()
        assertThat(randomDC.short).isNotNull()
        assertThat(randomDC.byte).isNotNull()
        assertThat(randomDC.boolean).isNotNull()
        assertThat(randomDC.string).isNotNull()
    }

    @Test
    fun `Should generate random values for Kotlin Basic Types using the reified syntax`() {
        val randomDC: BasicTypesDC = minRandom()

        assertThat(randomDC.double).isNotNull()
        assertThat(randomDC.float).isNotNull()
        assertThat(randomDC.long).isNotNull()
        assertThat(randomDC.int).isNotNull()
        assertThat(randomDC.short).isNotNull()
        assertThat(randomDC.byte).isNotNull()
        assertThat(randomDC.boolean).isNotNull()
        assertThat(randomDC.string).isNotNull()
    }

    data class DCWithNestedDC(val normalField: String, val nestedDC: NestedDC)

    data class NestedDC(val normalField: String)

    @Test
    fun `Should generate nested DCs`() {
        val randomDCWithNestedDC = DCWithNestedDC::class.minRandom()

        assertThat(randomDCWithNestedDC.nestedDC.normalField).isNotNull()
    }

    data class UnsupportedTypeDC(val sqlData: SQLData)

    @Test
    fun `Should throw RuntimeException when class contains unsupported type`() {
        assertFailure { UnsupportedTypeDC::class.minRandom() }
            .isInstanceOf<RuntimeException>()
            .hasMessage(
                "Could not generate random instance of class java.sql.SQLData. You can supply your " +
                    "own instance of this class by using KMinRandom.supplyValueForClass().",
            )
    }

    @Test
    fun `Should generate random instance of Kotlin Basic Type`() {
        val randomInt = Int::class.minRandom()

        assertThat(randomInt).isNotNull()
    }

    data class NullableTypesDC(val unsupportedType: SQLData?, val string: String?)

    @Test
    fun `Should generate null values for unsupported types if they are nullable`() {
        val randomDC = NullableTypesDC::class.minRandom()

        assertThat(randomDC.unsupportedType).isNull()
        assertThat(randomDC.string).isNull()
    }

    @Test
    fun `Should generate unique values`() {
        KMinRandom.removeSupportForClass(String::class)
        assertThat(String::class.minRandom()).isNotEqualTo(String::class.minRandom())
    }
}
