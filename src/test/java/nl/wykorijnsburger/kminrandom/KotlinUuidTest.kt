package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import kotlin.test.Test
import kotlin.uuid.Uuid

internal class KotlinUuidTest {
    data class KotlinUuidDC(val uuid: Uuid)

    @Test
    fun `Should generate random values for Kotlin Uuid`() {
        val randomDC = KotlinUuidDC::class.minRandom()

        assertThat(randomDC.uuid).isNotNull()
        assertThat(randomDC.uuid).isNotEqualTo(Uuid.NIL)
    }

    @Test
    fun `Should generate different Kotlin Uuids`() {
        assertThat(minRandom<Uuid>()).isNotEqualTo(minRandom<Uuid>())
    }
}
