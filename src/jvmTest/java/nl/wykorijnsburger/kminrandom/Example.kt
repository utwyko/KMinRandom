package nl.wykorijnsburger.kminrandom

import assertk.assertThat
import assertk.assertions.isTrue
import kotlin.test.Test

internal class ExampleTest {

    @Test
    fun `Without KMinRandom - isBlue() should return true when colour is blue`() {
        val car = Car(
            colour = "blue",
            model = "MODEL",
            speed = null,
            manufacturer = Manufacturer(
                name = "MANUFACTURER_NAME"
            )
        )

        assertThat(car.isBlue()).isTrue()
    }

    @Test
    fun `With KMinRandom - isBlue() should return true when colour is blue`() {
        val car = Car::class.minRandom()
            .copy(colour = "blue")

        assertThat(car.isBlue()).isTrue()
    }
}

data class Car(
    val colour: String,
    val model: String,
    val speed: Int?,
    val manufacturer: Manufacturer
) {
    fun isBlue() = colour == "blue"
}

data class Manufacturer(val name: String)
