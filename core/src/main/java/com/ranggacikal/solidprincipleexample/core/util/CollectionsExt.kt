package com.ranggacikal.solidprincipleexample.core.util

fun <T, A> pairOf(first: T, second: A? = null): Pair<T, A?> = Pair(first, second)

fun <T, A, B> tripleOf(first: T, second: A, third: B? = null): Triple<T, A, B?> = Triple(first, second, third)

//fun <T, A, B, C> quadrupleOf(first: T, second: A, third: B? = null, fourth: C? = null): Quadruple<T, A, B?, C?> =
//    Quadruple(first, second, third, fourth)
//
//fun <T> Quadruple<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)