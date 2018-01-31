package nl.wykorijnsburger.kminrandom

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import java.sql.SQLData
import java.time.Instant
import java.time.LocalDate
import java.util.*

class MinRandomTest {

    data class BasicTypesDC(
        val double: Double,
        val float: Float,
        val long: Long,
        val int: Int,
        val short: Short,
        val byte: Byte,
        val char: Char,
        val boolean: Boolean,
        val string: String
    )

    @Test
    fun `Should generate random values for Kotlin Basic Types`() {
        val randomDC = BasicTypesDC::class.minRandom()

        assertThat(randomDC.double).isNotNull()
        assertThat(randomDC.float).isNotNull()
        assertThat(randomDC.long).isNotNull()
        assertThat(randomDC.int).isNotNull()
        assertThat(randomDC.short).isNotNull()
        assertThat(randomDC.byte).isNotNull()
        assertThat(randomDC.boolean).isNotNull()
        assertThat(randomDC.string).isNotNull()
    }

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

    data class DatesDC(val date: Date,
                      val localDate: LocalDate,
                      val instant: Instant)

    @Test
    fun `Should generate random Date values`() {
        val randomDC = DatesDC::class.minRandom()

        assertThat(randomDC.date).isNotNull()
        assertThat(randomDC.localDate).isNotNull()
        assertThat(randomDC.instant).isNotNull()
    }

    data class NullableDC(
        val nullableString: String?,
        val nullableInt: Int?,
        val nullableListOfStrings: List<String>?,
        val nullableSetOfStrings: Set<String>?,
        val nullableMapOfStrings: Map<String, String>?,
        val nullableSequenceOfStrings: Sequence<String>?,
        val nullableQueueOfStrings: Queue<String>?
    )

    @Test
    fun `Should generate nullable fields as null`() {
        val randomDC = NullableDC::class.minRandom()

        assertThat(randomDC.nullableString).isNull()
        assertThat(randomDC.nullableInt).isNull()
        assertThat(randomDC.nullableListOfStrings).isNull()
        assertThat(randomDC.nullableSetOfStrings).isNull()
        assertThat(randomDC.nullableMapOfStrings).isNull()
        assertThat(randomDC.nullableSequenceOfStrings).isNull()
        assertThat(randomDC.nullableQueueOfStrings).isNull()
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

    data class SequenceDC(val sequence: Sequence<String>)

    @Test
    fun `Should generate sequences as empty sequences`() {
        val randomDC = SequenceDC::class.minRandom()

        assertThat(randomDC.sequence.count()).isEqualTo(0)
    }

    data class QueueDC(val queue: Queue<String>)

    @Test
    fun `Should generate queues as empty queues`() {
        val randomDC = QueueDC::class.minRandom()

        assertThat(randomDC.queue.count()).isEqualTo(0)
    }

    data class MapDC(
        val map: Map<String, String>,
        val mutableMap: MutableMap<String, String>
    )

    @Test
    fun `Should generate maps as empty maps`() {
        val randomDC = MapDC::class.minRandom()

        assertThat(randomDC.map).isEmpty()
        assertThat(randomDC.mutableMap).isEmpty()
    }

    data class DCWithNestedDC(
        val normalField: String,
        val nestedDC: NestedDC
    )

    data class NestedDC(val normalField: String)

    @Test
    fun `Should generate nested DCs`() {
        val randomDCWithNestedDC = DCWithNestedDC::class.minRandom()

        assertThat(randomDCWithNestedDC.nestedDC.normalField).isNotNull()
    }

    data class UnsupportedTypeDC(val sqlData: SQLData)

    @Test
    fun `Should throw RuntimeException when class contains unsupported type`() {
        assertThatThrownBy { UnsupportedTypeDC::class.minRandom() }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Could not generate random instance of class java.sql.SQLData")
    }

    @Test
    fun `Should generate random instance of Kotlin Basic Type`() {
        val randomInt = Int::class.minRandom()

        assertThat(randomInt).isNotNull()
    }
}