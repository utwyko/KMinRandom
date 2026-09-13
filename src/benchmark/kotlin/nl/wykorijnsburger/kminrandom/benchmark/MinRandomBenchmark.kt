package nl.wykorijnsburger.kminrandom.benchmark

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import nl.wykorijnsburger.kminrandom.minRandom
import nl.wykorijnsburger.kminrandom.minRandomCached
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import kotlin.random.Random
import kotlin.reflect.KClass

/**
 * Steady-state cost of generating a single instance, per code path.
 */
@Suppress("TooManyFunctions") // One function per benchmarked code path.
@State(Scope.Benchmark)
class MinRandomBenchmark {
    @Setup
    fun setup() {
        FlatDC::class.minRandomCached()
    }

    @Benchmark
    fun flat(): FlatDC = FlatDC::class.minRandom()

    @Benchmark
    fun wide(): WideDC = WideDC::class.minRandom()

    @Benchmark
    fun defaults(): DefaultsDC = DefaultsDC::class.minRandom()

    @Benchmark
    fun sealedClass(): Shape = Shape::class.minRandom()

    @Benchmark
    fun enumClass(): Color = Color::class.minRandom()

    @Benchmark
    fun objectInstance(): Singleton = Singleton::class.minRandom()

    @Benchmark
    fun javaClass(): BenchmarkJavaClass = BenchmarkJavaClass::class.minRandom()

    @Benchmark
    fun reified(): FlatDC = minRandom<FlatDC>()

    @Benchmark
    fun cachedHit(): FlatDC = FlatDC::class.minRandomCached()

    /**
     * Hand-written equivalent of [flat]: the lower bound for what generation could cost.
     */
    @Benchmark
    fun baselineFlat(): FlatDC = FlatDC(
        int = Random.nextInt(),
        long = Random.nextLong(),
        double = Random.nextDouble(),
        boolean = Random.nextBoolean(),
        string = randomLetters(),
        char = LETTERS.random(),
        float = Random.nextFloat(),
        short = Random.nextInt().toShort(),
    )

    private fun randomLetters(): String {
        val chars = CharArray(Random.nextInt(1, MAX_STRING_LENGTH)) { LETTERS.random() }
        return String(chars)
    }

    private companion object {
        const val LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        const val MAX_STRING_LENGTH = 50
    }
}

/**
 * How generation cost scales with nesting depth (fan-out 2, so leaves double per level).
 */
@State(Scope.Benchmark)
class NestedBenchmark {
    @Param("1", "2", "4", "6", "8")
    var depth: Int = 0

    private lateinit var nestedClass: KClass<*>

    @Setup
    fun setup() {
        nestedClass = nestedByDepth.getValue(depth)
    }

    @Benchmark
    fun nested(): Any = nestedClass.minRandom()
}

/**
 * The first call in a fresh JVM, dominated by kotlin-reflect initialisation.
 * Only meaningful in single-shot mode with many forks (see the `coldStart` configuration).
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
class ColdStartBenchmark {
    @Benchmark
    fun firstFlat(): FlatDC = FlatDC::class.minRandom()
}
