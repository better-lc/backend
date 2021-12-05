package moe.hypixel.lc.cosmetics

enum class CosmeticType(
	val sqlName: String
) {
	CAPE("cape"),
	HAT("hat"),
	MASK("mask"),
	BANDANNA("bandanna"),
	TIE("tie"),
	WINGS("wings"),
	BACKPACK("backpack");

	fun sqlName(): String {
		return "${sqlName}_id"
	}

	companion object {
		fun fromString(type: String, subType: String): CosmeticType {
			return when(type) {
				"cape" -> CAPE
				"hat" -> {
					return when(subType) {
						"mask" -> MASK
						else -> HAT
					}
				}
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
