package moe.hypixel.lc.cosmetics

enum class CosmeticType {
	CAPE,
	HAT,
	MASK,
	BANDANNA,
	TIE,
	WINGS,
	BACKPACK;

	companion object {
		fun fromString(type: String, subType: String): CosmeticType {
			return when(type) {
				"cape" -> CAPE
				"hat" -> HAT
				"mask" -> MASK
				"bandanna" -> BANDANNA
				"face_bandanna" -> BANDANNA
				"bodywear" -> {
					return when(subType) {
						"backpack" -> BACKPACK
						else -> TIE
					}
				}
				"dragon_wings" -> WINGS
				"backpack" -> BACKPACK
				else -> throw IllegalArgumentException("$type $subType was unexpected")
			}
		}
	}
}
