package moe.hypixel.lc.database.models.user

import moe.hypixel.lc.cosmetics.Cosmetic

enum class UserRank(
	val id: Int,
	val title: String,
	val colour: Int,
	val blacklistedCosmetic: List<Int>
) {
	DEFAULT(0, "Default", 16777215, listOf()),
	BETA(1, "Beta", 16558080, listOf()),
	PARTNER(2, "Partner", 16558080, listOf()),
	MOD(3, "Moderator", 43690, listOf()),
	ADMIN(4, "Admin", 16274259, listOf()),
	//TODO: Choose cma member colour
	CMA_MEMBER(5, "CMA Member", 1, listOf()),
	OWNER(6, "Owner", 11141120, listOf())
	;

	fun isBlacklistedCosmetic(cosmetic: Cosmetic): Boolean = blacklistedCosmetic.contains(cosmetic.id)
	fun canUseCosmetic(cosmetic: Cosmetic): Boolean = !isBlacklistedCosmetic(cosmetic)

	companion object {
		fun fromId(id: Int): UserRank? {
			return values().firstOrNull { it.id == id }
		}
	}
}