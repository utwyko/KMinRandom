package nl.wykorijnsburger.kminrandom

internal val privateConstructorException =
    RuntimeException("Cannot generate random instance of class with a private constructor. If you are trying to generate a random instance of a sealed class, try generating one the classes extending the sealed class.")

internal val noConstructorException = RuntimeException("Cannot generate random instance of class without a constructor")