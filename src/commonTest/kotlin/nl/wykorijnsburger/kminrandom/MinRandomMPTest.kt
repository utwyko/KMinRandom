package nl.wykorijnsburger.kminrandom

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class MinRandomMPTest {

    data class BasicTypesDC(
        val double: Double,
        val float: Float,
        val long: Long,
        val int: Int,
        val short: Short,
        val byte: Byte
//        val char: Char,
//        val boolean: Boolean,
//        val string: String
    )

    @Test
    fun `Should generate random String`() {
        val randomString = minRandomMP<String>()
        assertNotNull(randomString)
    }

    @Test
    fun `Should return random double`() {
        val randomDouble = minRandomMP<Double>()
        assertNotNull(randomDouble)
    }


    @Test
    fun `Should generate random values for Kotlin Basic Types`() {
        val randomDC = minRandomMP<BasicTypesDC>()

        assertNotNull(randomDC.double)
        assertNotNull(randomDC.float)
        assertNotNull(randomDC.long)
        assertNotNull(randomDC.int)
        assertNotNull(randomDC.short)
        assertNotNull(randomDC.byte)
    }
}
