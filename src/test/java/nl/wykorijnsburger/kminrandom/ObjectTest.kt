package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class ObjectTest {

    internal object SampleObject {
        const val objectValue = "OBJECT_VALUE"
    }

    @Test
    fun `Should return instance of object when using on Object`() {
        val randomObject = SampleObject::class.minRandom()

        assertThat(randomObject).isEqualTo(SampleObject)
        assertThat(randomObject.objectValue).isNotNull()
    }

    internal data class ObjectDC(
        val sampleObject: SampleObject
    )

    @Test
    fun `Should generate random instance of DC with object value`() {
        val randomDC = ObjectDC::class.minRandomCached()

        assertThat(randomDC.sampleObject).isEqualTo(SampleObject)
    }

    @Test
    fun `Should generate random instance of Sealed Class implemented by an Object`() {
        val randomSealedClass = SealedClass::class.minRandomCached()

        assertThat(randomSealedClass).isEqualTo(SampleObjectImplementingSealedClass)
    }
}

internal sealed class SealedClass
internal object SampleObjectImplementingSealedClass : SealedClass()
