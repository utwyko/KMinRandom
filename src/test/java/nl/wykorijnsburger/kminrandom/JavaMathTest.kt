package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isNotNull
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.Test

internal class JavaMathTest {

    data class JavaMathDC(
        val bigInteger: BigInteger,
        val bigDecimal: BigDecimal,
    )

    @Test
    fun `Should generate random values for Java Math types`() {
        val randomDC = JavaMathDC::class.minRandom()

        assertThat(randomDC.bigDecimal).isNotNull()
        assertThat(randomDC.bigInteger).isNotNull()
    }
}
