package nl.wykorijnsburger.kminrandom

import org.junit.Test


class EnumTest {

    data class EnumDC(val sampleEnum: SampleEnum)

    enum class SampleEnum {
        VALUE_1,
        VALUE_2
    }

    @Test
    fun `Should generate random enum values`() {
        EnumDC::class.minRandom()
    }
}