package nl.wykorijnsburger.kminrandom

import kotlin.reflect.KClass

internal val privateConstructorException =
    RuntimeException("Cannot generate random instance of class with a private constructor. If you are trying to generate a random instance of a sealed class, try generating one the classes extending the sealed class.")

internal val noConstructorException = RuntimeException("Cannot generate random instance of class without a constructor")

internal val selfReferentialException = RuntimeException("Cannot generate random instance of class that references itself")

internal fun unsupportedClassException(clazz: KClass<*>) = RuntimeException("Could not generate random instance of $clazz. You can supply your own instance of this class by using KMinRandom.supplyValueForClass().")