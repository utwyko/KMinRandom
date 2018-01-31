package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import java.sql.SQLData
import java.time.Instant
import java.time.LocalDate
import java.util.*

class MinRandomTest {

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


    data class DatesDC(val date: Date,
                      val localDate: LocalDate,
                      val instant: Instant)

    @Test
    fun `Should generate random Date values`() {
        val randomDC = DatesDC::class.minRandom()

        assertThat(randomDC.date).isNotNull()
        assertThat(randomDC.localDate).isNotNull()
        assertThat(randomDC.instant).isNotNull()
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
}