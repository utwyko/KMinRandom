internal fun randomString(): String {
    return (1..50)
        .map { charPool.random() }
        .joinToString("")
}

@Suppress("SpellCheckingInspection")
private const val charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
