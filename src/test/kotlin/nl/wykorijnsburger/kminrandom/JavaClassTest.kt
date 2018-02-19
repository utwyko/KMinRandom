package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class JavaClassTest {

    data class JavaClassDC(
        val sampleJavaClass: SampleJavaClass
    )

    @Test
    fun `Should be able to generate random instance of Java class`() {
        val randomDC = JavaClassDC::class.minRandom()

        assertThat(randomDC.sampleJavaClass).isNotNull()
    }

    @Test
    fun `Should use constructor with the least amount of arguments when instaniating a Java class`() {
        val randomDC = JavaClassDC::class.minRandom()

        assertThat(randomDC.sampleJavaClass.string1).isNotNull()
        assertThat(randomDC.sampleJavaClass.string2).isNull()
    }
}