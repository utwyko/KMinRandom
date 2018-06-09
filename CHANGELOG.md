# Change Log

## 0.0.7 (UNRELEASED)
* Add new `minRandomCached()` method that fetches an already cached generated instance from a cache if possible. 
* Throw descriptive exception when trying to generate random sealed class instance.

## 0.0.6 (2 May 2018)
* Fix direct random generation of Enum

## 0.0.5 (9 April 2018)
* Improve randomString implementation

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