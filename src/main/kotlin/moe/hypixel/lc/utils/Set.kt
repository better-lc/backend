package moe.hypixel.lc.utils

fun <T> notNullSet(vararg values: T?): MutableSet<T> = values.filterNotNull().toMutableSet()

fun <T, E> List<T>.unique(cb: (T) -> E): List<T> {
	val set = mutableSetOf<E>()
	val returns = mutableListOf<T>()

	for(item in this) {
		val cond = cb(item)
		if(set.contains(cond)) continue
		else set.add(cond)

		returns.add(item)
	}

	return returns
}