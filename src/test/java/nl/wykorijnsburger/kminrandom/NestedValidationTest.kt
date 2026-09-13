package nl.wykorijnsburger.kminrandom

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import nl.wykorijnsburger.kminrandom.exception.SelfReferentialException
import java.sql.SQLData
import kotlin.test.Test

internal class NestedValidationTest {
    data class Level3(val string: String, val nullable: String?)
    data class Level2(val left: Level3, val right: Level3)
    data class Level1(val left: Level2, val right: Level2)
    data class Root(val left: Level1, val right: Level1)

    @Test
    fun `Should fully generate deeply nested classes`() {
        val root = Root::class.minRandom()

        assertThat(root.right.left.right.string).isNotNull()
        assertThat(root.left.right.left.nullable).isNull()
    }

    data class DeepUnsupported3(val sqlData: SQLData)
    data class DeepUnsupported2(val level3: DeepUnsupported3)
    data class DeepUnsupported1(val string: String, val level2: DeepUnsupported2)

    @Test
    fun `Should throw unsupported class error for unsupported type deep in nesting`() {
        assertFailure { DeepUnsupported1::class.minRandom() }
            .hasMessage(
                "Could not generate random instance of class java.sql.SQLData. You can supply your " +
                    "own instance of this class by using KMinRandom.supplyValueForClass().",
            )
    }

    @Suppress("unused")
    data class DeepSelfReference(val string: String, val wrapper: Wrapper) {
        data class Wrapper(val inner: Inner)
        data class Inner(val wrapper: Wrapper)
    }

    @Test
    fun `Should throw self referential error for cycle below the root class`() {
        assertFailure { DeepSelfReference::class.minRandom() }
            .isInstanceOf<SelfReferentialException>()
    }

    sealed class SealedWithUnsupported {
        data class Only(val sqlData: SQLData) : SealedWithUnsupported()
    }

    data class SealedHolder(val sealed: SealedWithUnsupported)

    @Test
    fun `Should throw unsupported class error for unsupported type inside nested sealed subclass`() {
        assertFailure { SealedHolder::class.minRandom() }
            .hasMessage(
                "Could not generate random instance of class java.sql.SQLData. You can supply your " +
                    "own instance of this class by using KMinRandom.supplyValueForClass().",
            )
    }
}
