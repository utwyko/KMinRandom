package nl.wykorijnsburger.kminrandom

import kotlin.random.Random
import kotlin.streams.asSequence

internal fun randomString(): String {
    @Suppress("SpellCheckingInspection")
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    return random.ints(Random.nextInt(1, 50).toLong(), 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")
}
