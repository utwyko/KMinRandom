package nl.wykorijnsburger.kminrandom.exception

import kotlin.reflect.KClass

internal class PrivateConstructorException : RuntimeException() {
    override val message: String
        get() = "Cannot generate random instance of class with a private constructor. " +
            "If you are trying to generate a random instance of " +
            "a sealed class, try generating one the classes extending the sealed class."
}

internal class NoConstructorException : RuntimeException() {
    override val message: String
        get() = "Cannot generate random instance of class without a constructor"
}

internal class SelfReferentialException : RuntimeException() {
    override val message: String
        get() = "Cannot generate random instance of class that references itself"
}

internal class UnsupportedClassException(private val clazz: KClass<*>) : RuntimeException() {
    override val message: String
        get() = "Could not generate random instance of $clazz. You can supply your own instance of this class " +
            "by using KMinRandom.supplyValueForClass()."
}

internal class SuppliedValueException(private val clazz: KClass<*>) : RuntimeException() {
    override val message: String
        get() = "The provided value is not an instance of $clazz."
}
