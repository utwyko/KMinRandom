package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Date
import java.util.Optional
import java.util.UUID

internal class JavaUtilTest {

    data class JavaUtilDC(
        val optional: Optional<String>,
        val uuid: UUID,
        val date: Date
    )

    @Test
    fun `Should generate random values for Java Util types`() {
        val randomDC = JavaUtilDC::class.minRandom()

        assertThat(randomDC.optional).isEmpty
        assertThat(randomDC.uuid).isNotNull()
        assertThat(randomDC.date).isNotNull()
    }
}
