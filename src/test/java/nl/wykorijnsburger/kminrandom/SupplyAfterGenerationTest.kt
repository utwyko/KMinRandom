package nl.wykorijnsburger.kminrandom

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isSameInstanceAs
import nl.wykorijnsburger.kminrandom.exception.UnsupportedClassException
import kotlin.test.AfterTest
import kotlin.test.Test

/**
 * Removing a supplied value must take effect even for classes that were already generated before.
 * Supplying a value after a failed generation is covered by [SupplyTest].
 */
internal class SupplyAfterGenerationTest {
    interface RemovedLater
    object RemovedLaterImpl : RemovedLater
    data class RemovedLaterHolder(val removedLater: RemovedLater)
    data class RemovedLaterOuterHolder(val holder: RemovedLaterHolder)

    @AfterTest
    fun cleanUp() {
        KMinRandom.removeSupportForClass(RemovedLater::class)
    }

    @Test
    fun `Should fail for class that was generated before its supplied value was removed`() {
        KMinRandom.supplyValueForClass(RemovedLater::class, RemovedLaterImpl)
        assertThat(RemovedLaterOuterHolder::class.minRandom().holder.removedLater).isSameInstanceAs(RemovedLaterImpl)

        KMinRandom.removeSupportForClass(RemovedLater::class)

        assertFailure { RemovedLaterOuterHolder::class.minRandom() }
            .isInstanceOf<UnsupportedClassException>()
        assertFailure { RemovedLaterHolder::class.minRandom() }
            .isInstanceOf<UnsupportedClassException>()
    }
}
