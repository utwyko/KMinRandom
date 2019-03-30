# Change Log
## 0.10.0 (TBD)

## 0.0.9 (17 Dec 2018)
* Add support for `java.net.URL` and `java.net.URI`.

## 0.0.8 (2 Dec 2018)
* Upgrade to Kotlin 1.3, use Kotlin 1.3 Random implementation where possible
* Added the ability to generate a random instance of a [sealed class](https://kotlinlang.org/docs/reference/sealed-classes.html). A random subclass of the sealed class will be chosen and randomized.

## 0.0.7 (10 June 2018)
* Add new `minRandomCached()` method that fetches an already cached generated instance from a cache if possible. 
* Throw descriptive exception when trying to generate random sealed class instance.

## 0.0.6 (2 May 2018)
* Fix direct random generation of Enum

## 0.0.5 (9 April 2018)
* Improve randomString implementation1

## 0.0.4 (26 February 2018)
* Support for generating Java class members
* Support for LocalTime
* Use default values when available
* Use null for unsupported types if they are nullable

## 0.0.3 (15 February 2018)
* Add support for Optional, ZonedDateTime and UUID
* Add support for Enums
* Use default values when available

## 0.0.2 (10 February 2018)
* Added support for Java.Math types (BigDecimal and BigInteger)