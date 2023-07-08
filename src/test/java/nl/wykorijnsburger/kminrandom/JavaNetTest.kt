package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import java.net.URI
import java.net.URL

internal class JavaNetTest {

    data class JavaNetDC(
        val url: URL,
        val uri: URI
    )

    @Test
    fun `Should generate random values for Java Net types`() {
        val randomDC = JavaNetDC::class.minRandom()

        assertThat(randomDC.url).isNotNull()
        assertThat(randomDC.uri).isNotNull()
    }
}
