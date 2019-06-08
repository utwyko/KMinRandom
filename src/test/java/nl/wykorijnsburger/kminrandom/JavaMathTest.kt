package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

internal class JavaMathTest {

    data class JavaMathDC(
        val bigInteger: BigInteger,
        val bigDecimal: BigDecimal
    )

    @Test
    fun `Should generate random values for Java Math types`() {
        val randomDC = JavaMathDC::class.minRandom()

        assertThat(randomDC.bigDecimal).isNotNull()
        assertThat(randomDC.bigInteger).isNotNull()
    }
}