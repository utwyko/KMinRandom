package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import kotlin.test.Test

class SealedClassTest {
    @Test
    fun `Should generate a random instance of a randomly selected sealed class`() {
        val randomSealedClass = SampleSealedClass::class.minRandom()
        assertThat(randomSealedClass.sharedValue).isNotNull()
        assertThat(
            randomSealedClass is SealedClassImpl1 ||
                randomSealedClass is SealedClassImpl2,
        ).isTrue()
    }
}

sealed class SampleSealedClass {
    abstract val sharedValue: String
}

data class SealedClassImpl1(override val sharedValue: String, val nonSharedValue: String) : SampleSealedClass()

data class SealedClassImpl2(override val sharedValue: String, val nonSharedValue: String) : SampleSealedClass()
