package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@ExperimentalStdlibApi
internal class ExperimentalSyntaxTest {

    @Test
    fun `Should generate minRandom with experimental syntax`() {
        val randomDC = minRandom<BasicDC>()

        assertThat(randomDC.string).isNotNull()
        assertThat(randomDC.int).isNotNull()
    }

    @Test
    fun `Should generate minRandomCached with experimental syntax`() {
        val randomDC = minRandomCached<BasicDC>()

        assertThat(randomDC.string).isNotNull()
        assertThat(randomDC.int).isNotNull()
    }

    data class BasicDC(
        val string: String,
        val int: Int
    )
}
