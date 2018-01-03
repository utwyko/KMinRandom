package nl.wykorijnsburger.minimaltestobjects

import org.junit.Test


class MinRandomTest {

    @Test
    fun `Test`() {
        val randomPerson = minRandom(Person::class)

        println("randomPerson = $randomPerson")
    }

    data class Person(val id: String,
                      val age: Int,
                      val firstName: String?)
}