package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MinRandomTest {

    data class NullableDC(val nullableString: String?,
                          val nullableInt: Int?,
                          val nullableListOfStrings: List<String>?,
                          val nullableSetOfStrings: Set<String>?,
                          val nullableMapOfStrings: Map<String, String>?)

    @Test
    fun `Should generate nullable fields as null`() {
        val randomDC = NullableDC::class.minRandom()

        assertThat(randomDC.nullableString).isNull()
        assertThat(randomDC.nullableInt).isNull()
        assertThat(randomDC.nullableListOfStrings).isNull()
        assertThat(randomDC.nullableSetOfStrings).isNull()
        assertThat(randomDC.nullableMapOfStrings).isNull()
    }

    data class ListDC(val list: List<String>)

    @Test
    fun `Should generate lists as empty lists`() {
        val randomDC = ListDC::class.minRandom()

        assertThat(randomDC.list).isEmpty()
    }

    data class SetDC(val set: List<String>)

    @Test
    fun `Should generate sets as empty sets`() {
        val randomDC = SetDC::class.minRandom()

        assertThat(randomDC.set).isEmpty()
    }

    data class IterableDC(val iterable: Iterable<String>)

    @Test
    fun `Should generate iterables as empty iterables`() {
        val randomDC = IterableDC::class.minRandom()

        assertThat(randomDC.iterable).isEmpty()
    }

    data class MapDC(val map: Map<String, String>,
                     val mutableMap: MutableMap<String, String>)

    @Test
    fun `Should generate maps as empty maps`() {
        val randomDC = MapDC::class.minRandom()

        assertThat(randomDC.map).isEmpty()
        assertThat(randomDC.mutableMap).isEmpty()
    }

    data class DCWithNestedDC(val normalField: String,
                              val nestedDC: NestedDC)

    data class NestedDC(val normalField: String)

    @Test
    fun `Should generate nested DCs`() {
        val randomDCWithNestedDC = DCWithNestedDC::class.minRandom()

        assertThat(randomDCWithNestedDC.nestedDC.normalField).isNotNull()
    }
}