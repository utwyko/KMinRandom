package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.sql.SQLData

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
        val string: String
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

    data class DCWithNestedDC(
        val normalField: String,
        val nestedDC: NestedDC
    )

    data class NestedDC(val normalField: String)

    @Test
    fun `Should generate nested DCs`() {
        val randomDCWithNestedDC = DCWithNestedDC::class.minRandom()

        assertThat(randomDCWithNestedDC.nestedDC.normalField).isNotNull()
    }

    data class UnsupportedTypeDC(val sqlData: SQLData)

    @Test
    fun `Should throw RuntimeException when class contains unsupported type`() {
        assertThatThrownBy { UnsupportedTypeDC::class.minRandom() }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Could not generate random instance of class java.sql.SQLData")
    }

    @Test
    fun `Should generate random instance of Kotlin Basic Type`() {
        val randomInt = Int::class.minRandom()

        assertThat(randomInt).isNotNull()
    }

    data class NullableTypesDC(
        val unsupportedType: SQLData?,
        val string: String?
    )

    @Test
    fun `Should generate null values for unsupported types if they are nullable`() {
        val randomDC = NullableTypesDC::class.minRandom()

        assertThat(randomDC.unsupportedType).isNull()
        assertThat(randomDC.string).isNull()
    }
}