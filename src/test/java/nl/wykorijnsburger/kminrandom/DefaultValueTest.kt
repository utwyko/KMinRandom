package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class DefaultValueTest {

    data class DefaultValuesDC(
        val stringWithDefaultValue: String = "DEFAULT_VALUE",
        val intWithDefaultValue: Int = 10,
        val stringWithoutDefaultValue: String
    )

    @Test
    fun `Should use default values when available`() {
        val randomDC = DefaultValuesDC::class.minRandom()

        assertThat(randomDC.stringWithDefaultValue).isEqualTo("DEFAULT_VALUE")
        assertThat(randomDC.intWithDefaultValue).isEqualTo(10)
        assertThat(randomDC.stringWithDefaultValue).isNotNull()
    }
}
