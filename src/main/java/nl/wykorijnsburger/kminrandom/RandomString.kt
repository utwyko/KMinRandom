package nl.wykorijnsburger.kminrandom

import kotlin.random.Random
import kotlin.streams.asSequence

internal fun randomString(): String {
    @Suppress("SpellCheckingInspection")
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    return random
        .ints(Random.nextInt(1, MAX_RANDOM_STRING_LENGTH).toLong(), 0, source.length)
        .asSequence()
        .map(source::get)
        .joinToString("")
}

private const val MAX_RANDOM_STRING_LENGTH = 50
