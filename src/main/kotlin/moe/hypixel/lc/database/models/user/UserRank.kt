package moe.hypixel.lc.database.models.user

import moe.hypixel.lc.cosmetics.Cosmetic

enum class UserRank(
	val id: Int,
	val title: String,
	val colour: Int,
	val plus: Boolean,
	val blacklistedCosmetics: List<Int>
) {
	DEFAULT(0, "Default", 16777215, false, listOf()),
	BETA(1, "Beta", 16558080, true, listOf()),
	PARTNER(2, "Partner", 16558080, true, listOf()),
	MOD(3, "Moderator", 43690, true, listOf()),
	ADMIN(4, "Admin", 16274259, true, listOf()),
	//TODO: Choose cma member colour
	CMA_MEMBER(5, "CMA Member", 1, true, listOf()),
	OWNER(6, "Owner", 11141120, true, listOf())
	;

	fun isBlacklistedCosmetic(cosmetic: Cosmetic): Boolean = blacklistedCosmetics.contains(cosmetic.id)
	fun canUseCosmetic(cosmetic: Cosmetic): Boolean = !isBlacklistedCosmetic(cosmetic)

	companion object {
		fun fromId(id: Int): UserRank? {
			return values().firstOrNull { it.id == id }
		}
	}
}