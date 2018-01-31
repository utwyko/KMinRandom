package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class CollectionsTest {

    data class ArraysDC(
        val doubleArray: DoubleArray,
        val floatArray: FloatArray,
        val longArray: LongArray,
        val intArray: IntArray,
        val shortArray: ShortArray,
        val byteArray: ByteArray,
        val charArray: CharArray,
        val booleanArray: BooleanArray
    )

    @Test
    fun `Should generate random values for Arrays`() {
        val randomDC = ArraysDC::class.minRandom()

        assertThat(randomDC.doubleArray).isEmpty()
        assertThat(randomDC.floatArray).isEmpty()
        assertThat(randomDC.longArray).isEmpty()
        assertThat(randomDC.intArray).isEmpty()
        assertThat(randomDC.shortArray).isEmpty()
        assertThat(randomDC.byteArray).isEmpty()
        assertThat(randomDC.charArray).isEmpty()
        assertThat(randomDC.booleanArray).isEmpty()
    }

    data class NullableCollectionsDC(
        val nullableString: String?,
        val nullableInt: Int?,
        val nullableListOfStrings: List<String>?,
        val nullableSetOfStrings: Set<String>?,
        val nullableMapOfStrings: Map<String, String>?,
        val nullableSequenceOfStrings: Sequence<String>?,
        val nullableQueueOfStrings: Queue<String>?
    )

    @Test
    fun `Should generate nullable collections as null`() {
        val randomDC = NullableCollectionsDC::class.minRandom()

        assertThat(randomDC.nullableString).isNull()
        assertThat(randomDC.nullableInt).isNull()
        assertThat(randomDC.nullableListOfStrings).isNull()
        assertThat(randomDC.nullableSetOfStrings).isNull()
        assertThat(randomDC.nullableMapOfStrings).isNull()
        assertThat(randomDC.nullableSequenceOfStrings).isNull()
        assertThat(randomDC.nullableQueueOfStrings).isNull()
    }

    data class CollectionsDC(
        val list: List<String>,
        val set: Set<String>,
        val iterable: Iterable<String>,
        val sequence: Sequence<String>,
        val queue: Queue<String>,
        val map: Map<String, String>,
        val mutableMap: MutableMap<String, String>
    )

    @Test
    fun `Should generate collections as empty collections`() {
        val randomDC = CollectionsDC::class.minRandom()
        assertThat(randomDC.list).isEmpty()
        assertThat(randomDC.set).isEmpty()
        assertThat(randomDC.iterable).isEmpty()
        assertThat(randomDC.sequence.count()).isEqualTo(0)
        assertThat(randomDC.queue.count()).isEqualTo(0)
        assertThat(randomDC.map).isEmpty()
        assertThat(randomDC.mutableMap).isEmpty()
    }
}
