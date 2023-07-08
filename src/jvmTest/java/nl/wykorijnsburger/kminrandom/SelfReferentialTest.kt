package nl.wykorijnsburger.kminrandom

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import nl.wykorijnsburger.kminrandom.SelfReferentialTest.SelfReferentialThroughNestingDC.WrappingDC
import exception.SelfReferentialException
import kotlin.test.Test

internal class SelfReferentialTest {
    @Suppress("SelfReferenceConstructorParameter")
    data class SelfReferentialDC(val self: SelfReferentialDC)

    @Test
    fun `Should throw error when DC references itself`() {
        assertFailure { SelfReferentialDC::class.minRandomCached() }
            .isInstanceOf<SelfReferentialException>()
    }

    @Suppress("unused")
    data class SelfReferentialThroughNestingDC(val wrappingDC: WrappingDC) {
        data class WrappingDC(val selfReferentialThroughNestingDC: SelfReferentialThroughNestingDC)
    }

    @Test
    fun `Should throw error when DC references itself through nesting`() {
        assertFailure { WrappingDC::class.minRandomCached() }
            .isInstanceOf<SelfReferentialException>()
    }

    data class MultiplePairsDC(
        val pair1: Pair<Any, Any>,
        val pair2: Pair<Any, Any>
    )

    @Test
    fun `Should not throw error when multiple properties are of the same type`() {
        assertThat(MultiplePairsDC::class.minRandom()).isNotNull()
    }
}
