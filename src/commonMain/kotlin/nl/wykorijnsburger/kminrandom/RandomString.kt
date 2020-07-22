package nl.wykorijnsburger.kminrandom

import kotlin.random.Random

internal fun randomString(): String {
    @Suppress("SpellCheckingInspection")
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    val length = Random.nextInt(1, 50)
    return (0..length).map {
        source[Random.nextInt(source.length)]
    }.joinToString("")
}
