package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class ReifiedSyntaxTest {

    @Test
    fun `Should generate minRandom with reified syntax`() {
        val randomDC = minRandom<BasicDC>()

        assertThat(randomDC.string).isNotNull()
        assertThat(randomDC.int).isNotNull()
    }

    @Test
    fun `Should generate minRandomCached with reified syntax`() {
        val randomDC = minRandomCached<BasicDC>()

        assertThat(randomDC.string).isNotNull()
        assertThat(randomDC.int).isNotNull()
    }

    data class BasicDC(
        val string: String,
        val int: Int
    )
}
