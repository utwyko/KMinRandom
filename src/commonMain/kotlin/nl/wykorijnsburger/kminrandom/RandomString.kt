package nl.wykorijnsburger.kminrandom

import kotlin.random.Random

internal fun randomString(): String {
    return List(Random.nextInt(1, 50)) { Random.nextInt(0, letters.length)}
        .asSequence()
        .map(letters::get)
        .joinToString("")
}

@Suppress("SpellCheckingInspection")
private val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
