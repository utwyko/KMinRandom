package nl.wykorijnsburger.kminrandom

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import java.sql.SQLData
import java.sql.SQLInput
import java.sql.SQLOutput
import kotlin.test.Test

internal class SupplyTest {

    private val sqlData = object : SQLData {
        override fun readSQL(stream: SQLInput?, typeName: String?) = Unit

        override fun getSQLTypeName(): String {
            return "SQL_TYPE_NAME"
        }

        override fun writeSQL(stream: SQLOutput?) = Unit
    }

    init {
        KMinRandom.removeSupportForClass(SQLData::class)
    }

    @Test
    fun `Should generate a random instance of an unsupported class by supplying a value for unsupported class`() {
        assertFailure { SQLData::class.minRandom() }
            .isInstanceOf<RuntimeException>()

        KMinRandom.supplyValueForClass(SQLData::class, sqlData)

        assertThat(SQLData::class.minRandom()).isEqualTo(sqlData)
    }

    internal data class SQLDataDC(private val sqlData: SQLData)

    @Test
    fun `Should generate DCs with unsupported values by supplying a value for unsupported class`() {
        assertFailure { SQLDataDC::class.minRandom() }
            .isInstanceOf<RuntimeException>()

        KMinRandom.supplyValueForClass(SQLData::class, sqlData)

        assertThat(SQLDataDC::class.minRandom()).isNotNull()
    }

    @Test
    fun `Should override randomized value with supplied value`() {
        val randomizedString = String::class.minRandom()

        KMinRandom.supplyValueForClass(String::class, "SUPPLIED_STRING")

        val randomStringAfterSupply = String::class.minRandom()

        assertThat(randomizedString).isNotEqualTo(randomStringAfterSupply)
        assertThat(randomStringAfterSupply).isEqualTo("SUPPLIED_STRING")
    }
}
