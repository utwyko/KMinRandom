package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MinRandomCachedTest {

    data class SampleDC(
        val value1: String,
        val value2: String
    )

    @Test
    fun `Should cache random instance when using the minRandomCached method`() {
        val newMinRandom = SampleDC::class.minRandomCached()
        (0..10)
            .forEach { val minRandomCached = SampleDC::class.minRandomCached()
                assertThat(minRandomCached).isEqualTo(newMinRandom)
            }
    }
}