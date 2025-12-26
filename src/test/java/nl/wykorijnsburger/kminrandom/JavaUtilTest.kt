package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotNull
import java.util.*
import kotlin.test.Test

internal class JavaUtilTest {
    data class JavaUtilDC(val optional: Optional<String>, val uuid: UUID, val date: Date)

    @Test
    fun `Should generate random values for Java Util types`() {
        val randomDC = JavaUtilDC::class.minRandom()

        assertThat(randomDC.optional).isEmpty()
        assertThat(randomDC.uuid).isNotNull()
        assertThat(randomDC.date).isNotNull()
    }
}
