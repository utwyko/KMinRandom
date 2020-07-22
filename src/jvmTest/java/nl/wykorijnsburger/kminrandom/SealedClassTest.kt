package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SealedClassTest {

    @Test
    fun `Should generate a random instance of a randomly selected sealed class`() {
        val randomSealedClass = SampleSealedClass::class.minRandom()
        assertThat(randomSealedClass.sharedValue).isNotNull()
        assertThat(randomSealedClass).isInstanceOfAny(SealedClassImpl1::class.java, SealedClassImpl2::class.java)
    }
}

sealed class SampleSealedClass {
    abstract val sharedValue: String
}

data class SealedClassImpl1(
    override val sharedValue: String,
    val nonSharedValue: String
) : SampleSealedClass()

data class SealedClassImpl2(
    override val sharedValue: String,
    val nonSharedValue: String
) : SampleSealedClass()
