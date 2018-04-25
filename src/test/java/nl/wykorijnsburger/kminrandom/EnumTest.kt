package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class EnumTest {

    data class EnumDC(val sampleEnum: SampleEnum,
                      val sampleJavaEnum: SampleJavaEnum)

    enum class SampleEnum {
        VALUE_1,
        VALUE_2
    }

    @Test
    fun `Should generate random enum values`() {
        val randomDC = EnumDC::class.minRandom()

        assertThat(SampleEnum.values()).contains(randomDC.sampleEnum)
        assertThat(SampleJavaEnum.values()).contains(randomDC.sampleJavaEnum)
    }
}