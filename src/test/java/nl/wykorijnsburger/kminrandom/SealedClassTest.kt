package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test


class SealedClassTest {

    @Test
    fun `Should throw exception explaining that sealed class are not supported`() {
        assertThatThrownBy { SampleSealedClass::class.minRandom() }
            .hasMessage("Cannot generate random instance of class with a private constructor. " +
                    "If you are trying to generate a random instance of a sealed class, try generating one the classes extending the sealed class.")
    }
}

sealed class SampleSealedClass {
    abstract val sharedValue: String
}

data class SealedClassImpl(
    override val sharedValue: String,
    val nonSharedValue: String
) : SampleSealedClass()