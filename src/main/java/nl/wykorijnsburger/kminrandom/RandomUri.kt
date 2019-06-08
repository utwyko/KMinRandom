package nl.wykorijnsburger.kminrandom

import java.net.URI

internal fun randomURI() = URI(validSchemes.random(), randomString(), "/${randomString()}", null)

/**
 * Source: https://docs.oracle.com/javase/7/docs/api/java/net/URL.html
 */
private val validSchemes = setOf("http", "https", "ftp", "file")