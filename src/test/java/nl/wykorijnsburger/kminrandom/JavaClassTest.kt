package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import kotlin.test.Test

internal class JavaClassTest {
    data class JavaClassDC(val sampleJavaClass: SampleJavaClass)

    @Test
    fun `Should be able to generate random instance of Java class`() {
        val randomDC = JavaClassDC::class.minRandom()

        assertThat(randomDC.sampleJavaClass).isNotNull()
    }

    @Test
    fun `Should use constructor with the least amount of arguments when instantiating a Java class`() {
        val randomDC = JavaClassDC::class.minRandom()

        assertThat(randomDC.sampleJavaClass.string1).isNotNull()
        assertThat(randomDC.sampleJavaClass.string2).isNull()
    }
}
