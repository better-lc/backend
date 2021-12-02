package moe.hypixel.lc.database.models.user

import java.util.*

enum class UserFlag(val bits: Int) {
	DEFAULT(0)
}

typealias UserFlags = EnumSet<UserFlag>

fun UserFlags.toBits(): Int {
	var result = UserFlag.DEFAULT.bits

	for(flag in this.iterator())
		result = result or flag.bits

	return result
}

fun fromBits(bits: Int): UserFlags {
	val flags = mutableSetOf<UserFlag>()

	for(flag in UserFlag.values()) {
		if((bits and flag.bits) == flag.bits)
			flags.add(flag)
	}

	return UserFlags.copyOf(flags)
}

infix fun UserFlags.allOf(other: UserFlags) = this.containsAll(other)
infix fun UserFlags.and(other: UserFlag) = UserFlags.of(other, *this.toTypedArray())
infix fun UserFlags.has(flag: UserFlag) = this.contains(flag)