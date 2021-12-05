package moe.hypixel.lc.database.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.hypixel.lc.cosmetics.CosmeticType
import moe.hypixel.lc.database.models.user.UserFlags
import moe.hypixel.lc.database.models.user.UserRank
import moe.hypixel.lc.database.serializers.UserFlagsSerializer
import org.bson.types.ObjectId
import java.util.*

@Serializable
data class User(
	@SerialName("_id")
	@Contextual
	var _id: ObjectId?,

	@Contextual
	var uuid: UUID,

	var rank: UserRank,

	@Serializable(UserFlagsSerializer::class)
	var flags: UserFlags,
	var discordId: String?,
	var cosmetics: MutableMap<CosmeticType, Int>,
	var equippedEmotes: MutableList<Int>,
	var settings: MutableMap<String, String>
) {
	var id: ObjectId
		get() = _id!!
		set(value) {
			_id = value
		}
}