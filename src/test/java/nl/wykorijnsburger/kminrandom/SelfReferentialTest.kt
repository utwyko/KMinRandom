package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isNotNull
import nl.wykorijnsburger.kminrandom.SelfReferentialTest.SelfReferentialThroughNestingDC.WrappingDC
import nl.wykorijnsburger.kminrandom.exception.SelfReferentialException
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class SelfReferentialTest {
    @Suppress("SelfReferenceConstructorParameter")
    data class SelfReferentialDC(val self: SelfReferentialDC)

    @Test
    fun `Should throw error when DC references itself`() {
        assertFailsWith<SelfReferentialException> { SelfReferentialDC::class.minRandomCached() }
    }

    @Suppress("unused")
    data class SelfReferentialThroughNestingDC(val wrappingDC: WrappingDC) {
        data class WrappingDC(val selfReferentialThroughNestingDC: SelfReferentialThroughNestingDC)
    }

    @Test
    fun `Should throw error when DC references itself through nesting`() {
        assertFailsWith<SelfReferentialException> { WrappingDC::class.minRandomCached() }
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
