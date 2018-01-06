package nl.wykorijnsburger.kminrandom

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.streams.asSequence

private val random = Random()

/**
 * Generates a minimal random instance of the
 */
fun <T : Any> KClass<T>.minRandom() = generateMinRandom(this)

fun <T : Any> generateMinRandom(clazz: KClass<T>): T {
    val parameters = clazz.primaryConstructor!!.parameters

    val constructorArguments = parameters
            .map {
                val type = it.type
                val listType = it.type.classifier?.starProjectedType
                when {
                    type.isMarkedNullable -> null
                    type == String::class.createType() -> randomString()
                    type == Int::class.createType() -> random.nextInt()
                    type == Long::class.createType() -> random.nextLong()
                    type == Float::class.createType() -> random.nextFloat()
                    type == Double::class.createType() -> random.nextDouble()
                    type == Boolean::class.createType() -> random.nextBoolean()
                    listType == List::class.starProjectedType -> emptyList<Any>()
                    listType == Set::class.starProjectedType -> emptySet<Any>()
                    listType == Iterable::class.starProjectedType -> emptyList<Any>()
                    listType == Map::class.starProjectedType -> emptyMap<Any, Any>()
                    else -> generateMinRandom(type.jvmErasure)
                }
            }

    return clazz.primaryConstructor!!.call(*constructorArguments.toTypedArray())
}

private fun randomString(): String {
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    return random.ints(10, 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")
}