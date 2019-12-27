# KMinRandom

[![Build Status](https://travis-ci.org/utwyko/KMinRandom.svg?branch=master)](https://travis-ci.org/utwyko/KMinRandom)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.wykorijnsburger.kminrandom/kminrandom/badge.svg)](https://maven-badges.herokuapp.com/maven-central/nl.wykorijnsburger.kminrandom/kminrandom)

KMinRandom is a library that generates minimal instances of [Kotlin data classes](https://kotlinlang.org/docs/reference/data-classes.html) for use in tests. A minimal implementation means:

* Nullable values will be null
* Collections will be empty collections
* Other supported values will be randomized

## Motivation
The goal of KMinRandom is to make tests more readable by making it easy to instantiate data classes. This eliminates the need to set properties unrelated to the logic that is being tested.

In Kotlin, easily instantiable classes could be achieved by adding default values to data classes. However, this adds the risk of DCs being instantiated without all required values being set. Furthermore, when an additional property with a default value is added to the DC, the compiler will not force you to set the value in each existing contructor invocation. 

## Usage
KMinRandom provides two extension methods on `KClass`.

* Use `.minRandom()` to generate a new random instance on each usage
* Use `.minRandomCached()` to reuse the same random instance across multiple tests. This will improve the performance of your tests and should be used if you do not care about the uniqueness of the generated instances.

KMinRandom contains a list of classes it can generate ([see code](https://github.com/utwyko/KMinRandom/blob/master/src/main/java/nl/wykorijnsburger/kminrandom/MinRandomizers.kt#L18)). For unknown values (such as your custom data classes), it will try to generate a random value for each of its non-optional constructor parameters.

If certain classes can not be generated, an exception will be thrown. You can supply your own instance of the unsupported class by using `KMinRandom.supplyValueForClass()`.


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

## Download

Gradle:
```groovy
testImplementation 'nl.wykorijnsburger.kminrandom:kminrandom:1.0.1'
```
or Maven:
```xml
<dependency>
  <groupId>nl.wykorijnsburger.kminrandom</groupId>
  <artifactId>kminrandom</artifactId>
  <version>1.0.1</version>
  <scope>test</scope>
</dependency>
```

## License
    Copyright 2018 Wyko Rijnsburger
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
