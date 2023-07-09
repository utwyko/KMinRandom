package nl.wykorijnsburger.kminrandom

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import kotlin.test.Test

internal class EnumTest {

    data class EnumDC(
        val sampleEnum: SampleEnum,
        val sampleJavaEnum: SampleJavaEnum,
    )

    enum class SampleEnum {
        VALUE_1,
        VALUE_2,
    }

    enum class EmptyEnum

    @Test
    fun `Should generate random enum properties in DC`() {
        val randomDC = EnumDC::class.minRandom()

        assertThat(SampleEnum.values()).contains(randomDC.sampleEnum)
        assertThat(SampleJavaEnum.values()).contains(randomDC.sampleJavaEnum)
    }

    @Test
    fun `Should generate random enum instance`() {
        assertThat(SampleEnum.values()).contains(SampleEnum::class.minRandom())
        assertThat(SampleJavaEnum.values()).contains(SampleJavaEnum::class.minRandom())
        assertFailure { EmptyEnum::class.minRandom() }
            .isInstanceOf<RuntimeException>()
            .hasMessage("Cannot generate random value for empty enum EmptyEnum")
    }
}
