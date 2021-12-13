package moe.hypixel.lc.database.models.user

import moe.hypixel.lc.cosmetics.Cosmetic

enum class UserRank(
	val title: String,
	val colour: Int,
	val blacklistedCosmetics: List<Int>
) {
	DEFAULT("Default", 16777215, listOf()),
	BETA("Beta", 16558080, listOf()),
	PARTNER( "Partner", 16558080, listOf()),
	MOD( "Moderator", 43690, listOf()),
	ADMIN( "Admin", 16274259, listOf()),
	//TODO: Choose cma member colour
	//https://steamcommunity.com/groups/CMAssociation
	CMA_MEMBER( "CMA Member", 1, listOf()),
	OWNER( "Owner", 11141120, listOf())
	;

	fun isBlacklistedCosmetic(cosmetic: Cosmetic): Boolean = blacklistedCosmetics.contains(cosmetic.id)
	fun canUseCosmetic(cosmetic: Cosmetic): Boolean = !isBlacklistedCosmetic(cosmetic)
}