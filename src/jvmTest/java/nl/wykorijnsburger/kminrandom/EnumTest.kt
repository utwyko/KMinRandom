package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class EnumTest {

    data class EnumDC(
        val sampleEnum: SampleEnum,
        val sampleJavaEnum: SampleJavaEnum
    )

    enum class SampleEnum {
        VALUE_1,
        VALUE_2
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
        assertThatThrownBy { EmptyEnum::class.minRandom() }
            .hasMessage("Cannot generate random value for empty enum EmptyEnum")
    }
}
