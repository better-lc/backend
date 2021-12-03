package moe.hypixel.lc.cosmetics

data class Cosmetic(
	var id: Long,
	var someFloat: Float,
	var texture: String,
	var name: String,
	var type: CosmeticType,
	var someBool: Boolean,
	var idk: String,
	var otherTexture: String
)