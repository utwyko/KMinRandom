package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotNull
import kotlin.test.Test
import java.util.Date
import java.util.Optional
import java.util.UUID

internal class JavaUtilTest {

    data class JavaUtilDC(
        val optional: Optional<String>,
        val uuid: UUID,
        val date: Date,
    )

    @Test
    fun `Should generate random values for Java Util types`() {
        val randomDC = JavaUtilDC::class.minRandom()

        assertThat(randomDC.optional).isEmpty()
        assertThat(randomDC.uuid).isNotNull()
        assertThat(randomDC.date).isNotNull()
    }
}
