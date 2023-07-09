package nl.wykorijnsburger.kminrandom

import kotlin.reflect.KClass
import kotlin.reflect.KType

internal actual fun platformSpecificClassToMinRandom() = mapOf<KClass<*>, () -> Any>(
)

internal actual fun <T : Any> KClass<T>.randomEnum(): T? {
    return null
}

internal actual fun KType.randomEnum(): Any? {
    return null
}
