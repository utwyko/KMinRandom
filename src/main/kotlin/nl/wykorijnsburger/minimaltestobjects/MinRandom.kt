package nl.wykorijnsburger.minimaltestobjects

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor
import kotlin.streams.asSequence

private val random = Random()

fun <T : Any> minRandom(type: KClass<T>): T? {
    val parameters = type.primaryConstructor!!.parameters

    val constructorArguments = parameters
            .map {
                val type = it.type
                when {
                    type.isMarkedNullable -> null
                    type == String::class.createType() -> randomString()
                    type == Int::class.createType() -> random.nextInt()
                    else -> null
                }
            }

    return type.primaryConstructor?.call(*constructorArguments.toTypedArray())
}

private fun randomString(): String {
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    return random.ints(10, 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")
}