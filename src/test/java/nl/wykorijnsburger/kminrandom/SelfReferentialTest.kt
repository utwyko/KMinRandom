package nl.wykorijnsburger.kminrandom

import nl.wykorijnsburger.kminrandom.SelfReferentialTest.SelfReferentialThroughNestingDC.WrappingDC
import nl.wykorijnsburger.kminrandom.exception.SelfReferentialException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class SelfReferentialTest {
    @Suppress("SelfReferenceConstructorParameter")
    data class SelfReferentialDC(val self: SelfReferentialDC)

    @Test
    fun `Should throw error when DC references itself`() {
        assertThatThrownBy { SelfReferentialDC::class.minRandomCached() }
            .isInstanceOf(SelfReferentialException::class.java)
    }

    data class SelfReferentialThroughNestingDC(val wrappingDC: WrappingDC) {
        data class WrappingDC(val selfReferentialThroughNestingDC: SelfReferentialThroughNestingDC)
    }

    @Test
    fun `Should throw error when DC references itself through nesting`() {
        assertThatThrownBy { WrappingDC::class.minRandomCached() }
            .isInstanceOf(SelfReferentialException::class.java)
    }

    data class MultiplePairsDC(
        val pair1: Pair<Any, Any>,
        val pair2: Pair<Any, Any>
    )
    @Test
    fun `Should not throw error when multiple properties are of the same type`() {
        assertThat(MultiplePairsDC::class.minRandom()).isNotNull
    }
}