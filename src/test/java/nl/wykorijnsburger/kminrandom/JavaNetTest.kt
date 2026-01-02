package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isNotNull
import java.net.URI
import java.net.URL
import kotlin.test.Test

internal class JavaNetTest {
    data class JavaNetDC(val url: URL, val uri: URI)

    @Test
    fun `Should generate random values for Java Net types`() {
        val randomDC = JavaNetDC::class.minRandom()

        assertThat(randomDC.url).isNotNull()
        assertThat(randomDC.uri).isNotNull()
    }
}
