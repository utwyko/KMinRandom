package nl.wykorijnsburger.kminrandom

import nl.wykorijnsburger.kminrandom.SelfReferentialTest.SelfReferentialThroughNestingDC.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class SelfReferentialTest {
    data class SelfReferentialDC(val self: SelfReferentialDC)

    @Test
    fun `Should throw error when DC references itself`() {
        assertThatThrownBy { SelfReferentialDC::class.minRandomCached() }
            .isEqualTo(selfReferentialException)
    }

    data class SelfReferentialThroughNestingDC(val wrappingDC: WrappingDC) {
        data class WrappingDC(val selfReferentialThroughNestingDC: SelfReferentialThroughNestingDC)
    }

    @Test
    fun `Should throw error when DC references itself through nesting`() {
        assertThatThrownBy { WrappingDC::class.minRandomCached() }
            .isEqualTo(selfReferentialException)
    }
}