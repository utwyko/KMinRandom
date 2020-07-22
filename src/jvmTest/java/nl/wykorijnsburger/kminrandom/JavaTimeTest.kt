package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

internal class JavaTimeTest {
    data class TimeDC(
        val localDate: LocalDate,
        val localTime: LocalTime,
        val instant: Instant,
        val zonedDateTime: ZonedDateTime
    )

    @Test
    fun `Should generate random Date values`() {
        val randomDC = TimeDC::class.minRandom()

        assertThat(randomDC.localDate).isNotNull()
        assertThat(randomDC.localTime).isNotNull()
        assertThat(randomDC.instant).isNotNull()
        assertThat(randomDC.zonedDateTime).isNotNull()
    }
}
