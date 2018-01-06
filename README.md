# KMinRandom

KMinRandom is a library that generates minimal instances of [Kotlin data classes](https://kotlinlang.org/docs/reference/data-classes.html) for use in tests. A minimal implementation means:

* Nullable values will be null
* Collections will be empty collections

## Motivation
The goal of KMinRandom is to make tests more readable.

## Example Usage

```kotlin
data class Car(val colour: String,
               val model: String,
               val speed: Int?,
               val manufacturer: Manufacturer) {
    fun isBlue() = colour == "blue"
}

data class Manufacturer(val name: String)

class ExampleTest {

    @Test
    fun `Without KMinRandom - isBlue() should return true when colour is blue`() {
        val car = Car(colour = "blue",
                model = "MODEL",
                speed = null,
                manufacturer = Manufacturer(
                        name = "MANUFACTURER_NAME"
                ))

        assertThat(car.isBlue()).isTrue()
    }

    @Test
    fun `With KMinRandom - isBlue() should return true when colour is blue`() {
        val car = Car::class.minRandom()
                .copy(colour = "blue")

        assertThat(car.isBlue()).isTrue()
    }
}
```