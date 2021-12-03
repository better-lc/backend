package moe.hypixel.lc.utils

fun <T> notNullSet(vararg values: T?): MutableSet<T> = values.filterNotNull().toMutableSet()
