package moe.hypixel.lc.database

import kotlin.reflect.KMutableProperty0

fun nullError(prop: KMutableProperty0<*>): Nothing {
	throw DatabaseUnexpectedNullException(prop)
}