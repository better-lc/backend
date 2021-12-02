package moe.hypixel.lc.database.models.user

enum class UserRank(
	val id: Int,
	val title: String,
	val colour: Int
) {
	DEFAULT(0, "Default", 16777215),
	BETA(1, "Beta", 16558080),
	PARTNER(2, "Partner", 16558080),
	MOD(3, "Moderator", 43690),
	ADMIN(4, "Admin", 16274259),
	//TODO: Choose cma member colour
	CMA_MEMBER(5, "CMA Member", 1),
	OWNER(6, "Owner", 11141120)
	;

	companion object {
		fun fromId(id: Int): UserRank? {
			return values().firstOrNull { it.id == id }
		}
	}
}