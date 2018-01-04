package nl.wykorijnsburger.minimaltestobjects

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MinRandomTest {

    @Test
    fun `Should generate nullable fields as null`() {
        val randomDC = generateMinRandom(DC::class)

        assertThat(randomDC.nullableString).isNull()
        assertThat(randomDC.nullableInt).isNull()
        assertThat(randomDC.nullableListOfStrings).isNull()
    }

    @Test
    fun `Should generate min random objects using extension method`() {
        val randomDC = DC::class.minRandom()

        assertThat(randomDC).isNotNull()
    }

    @Test
    fun `Should generate lists as empty lists`() {
        val randomDC = DC::class.minRandom()

        assertThat(randomDC.listOfStrings).isEmpty()
    }

    @Test
    fun `Should generate nested DCs`() {
        val randomDCWithNestedDC = DCWithNestedDC::class.minRandom()

        assertThat(randomDCWithNestedDC.nestedDC.normalField).isNotNull()
    }

    data class DC(val nonNullableString: String,
                  val nullableString: String?,
                  val nonNullableInt: Int,
                  val nullableInt: Int?,
                  val listOfStrings: List<String>,
                  val nullableListOfStrings: List<String>?)

    data class DCWithNestedDC(val normalField: String,
                              val nestedDC: NestedDC)

    data class NestedDC(val normalField: String)
}