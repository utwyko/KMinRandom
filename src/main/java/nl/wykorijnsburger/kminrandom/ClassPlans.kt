package nl.wykorijnsburger.kminrandom

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.KVisibility
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

/*
 * Reflection lookups are slow, so everything about a class that can't change at runtime is looked up once per class
 * and cached. Whether a class is supported depends on the supplied values, so that is cached separately in
 * checkedClasses and cleared when support for a class is removed.
 */

internal sealed interface ClassKind {
    class ObjectInstance(val instance: Any) : ClassKind

    class Sealed(val subclasses: List<KClass<*>>) : ClassKind

    class Regular(val isEnum: Boolean) : ClassKind
}

internal class ConstructorPlan<T : Any>(val constructor: KFunction<T>?) {
    val isPrivate: Boolean = constructor?.visibility == KVisibility.PRIVATE

    val requiredParameters: List<ParameterPlan> =
        constructor?.parameters.orEmpty().filter { !it.isOptional }.map(::ParameterPlan)
}

internal class ParameterPlan(val parameter: KParameter) {
    private val type: KType = parameter.type

    val isNullable: Boolean = type.isMarkedNullable

    val classifier: KClassifier? = type.classifier

    // Resolved lazily, as nullable parameters are never generated and their types never need to be inspected.
    val erasure: KClass<*> by lazy(LazyThreadSafetyMode.PUBLICATION) { type.jvmErasure }

    val isEnum: Boolean by lazy(LazyThreadSafetyMode.PUBLICATION) { erasure.java.isEnum }

    /**
     * The class to generate when no value is supplied for [classifier]: the class itself, or a type parameter's bound.
     */
    val generatedClass: KClass<*> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        checkNotNull(classifier).starProjectedType.jvmErasure
    }
}

private val classKinds = ConcurrentHashMap<KClass<*>, ClassKind>()

private val constructorPlans = ConcurrentHashMap<KClass<*>, ConstructorPlan<*>>()

/**
 * Classes for which all nested constructor parameter types were found to be supported and not self-referential.
 */
internal val checkedClasses: MutableSet<KClass<*>> = ConcurrentHashMap.newKeySet()

internal fun KClass<*>.kind(): ClassKind = classKinds.getOrPut(this) {
    val objectInstance = objectInstance
    when {
        objectInstance != null -> ClassKind.ObjectInstance(objectInstance)
        sealedSubclasses.isNotEmpty() -> ClassKind.Sealed(sealedSubclasses)
        else -> ClassKind.Regular(isEnum = java.isEnum)
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> KClass<T>.constructorPlan(): ConstructorPlan<T> =
    constructorPlans.getOrPut(this) { ConstructorPlan(getConstructorWithTheLeastArguments()) } as ConstructorPlan<T>

private fun <T : Any> KClass<T>.getConstructorWithTheLeastArguments(): KFunction<T>? =
    primaryConstructor ?: constructors.minByOrNull { it.parameters.size }
