package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class MinRandomCachedTest {

    data class SampleDC(
        val value1: String,
        val value2: String
    )

    @Test
    fun `Should cache random instance when using the minRandomCached method`() {
        val newMinRandom = SampleDC::class.minRandomCached()
        repeat(10) {
            val minRandomCached = SampleDC::class.minRandomCached()
            assertThat(minRandomCached).isEqualTo(newMinRandom)
        }
    }

    @Test
    fun `Should cache random instance when using the reified minRandomCached method`() {
        val newMinRandom = minRandomCached<SampleDC>()
        repeat(10) {
            val minRandomCached = minRandomCached<SampleDC>()
            assertThat(minRandomCached).isEqualTo(newMinRandom)
        }
    }
}
