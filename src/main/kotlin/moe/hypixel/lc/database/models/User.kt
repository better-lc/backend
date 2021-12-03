package moe.hypixel.lc.database.models

import com.eatthepath.uuid.FastUUID
import com.github.jasync.sql.db.RowData
import moe.hypixel.lc.database.DatabaseDeserializable
import moe.hypixel.lc.database.database
import moe.hypixel.lc.database.models.user.UserFlags
import moe.hypixel.lc.database.models.user.UserRank
import moe.hypixel.lc.database.models.user.fromBits
import moe.hypixel.lc.database.nullError
import moe.hypixel.lc.utils.dashUUID
import moe.hypixel.lc.utils.notNullSet
import java.util.*

class User: DatabaseDeserializable {
	var id by database<UUID>()
	var rank by database<UserRank>()
	var flags by database<UserFlags>()

	var discordId: String? = null
	var capeId: Int? = null
	var hatId: Int? = null
	var maskId: Int? = null
	var bandannaId: Int? = null
	var tieId: Int? = null
	var wingsId: Int? = null
	var backpackId: Int? = null

	override fun deserialize(row: RowData) {
		id = FastUUID.parseUUID(row.getString("id")?.dashUUID() ?: nullError(this::id))
		discordId = row.getString("discord_id")

		rank = UserRank.fromId(
			row.getInt("rank") ?: nullError(this::rank)
		) ?: nullError(this::rank)

		flags = fromBits(row.getInt("flags") ?: nullError(this::flags))

		capeId = row.getInt("cape_id")
		hatId = row.getInt("hat_id")
		maskId = row.getInt("mask_id")
		bandannaId = row.getInt("bandanna_id")
		tieId = row.getInt("tie_id")
		wingsId = row.getInt("wings_id")
		backpackId = row.getInt("backpack_id")
	}

	fun hasCosmetics(): Boolean {
		return (
			capeId != null ||
			hatId != null ||
			maskId != null ||
			bandannaId != null ||
			tieId != null ||
			wingsId != null ||
			backpackId != null
		)
	}

	fun getCosmetics(): MutableSet<Int> {
		return notNullSet(
			capeId,
			hatId,
			maskId,
			bandannaId,
			tieId,
			wingsId,
			backpackId
		)
	}

	fun cosmeticEnabled(id: Int): Boolean {
		return getCosmetics().contains(id)
	}
}