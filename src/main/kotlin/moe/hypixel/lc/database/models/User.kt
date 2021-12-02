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
import java.util.*

class User: DatabaseDeserializable {
	var id by database<UUID>()
	var discordId: String? = null
	var rank by database<UserRank>()
	var flags by database<UserFlags>()

	override fun deserialize(row: RowData) {
		id = FastUUID.parseUUID(row.getString("id")?.dashUUID() ?: nullError(this::id))
		discordId = row.getString("discord_id")

		rank = UserRank.fromId(
			row.getInt("rank") ?: nullError(this::rank)
		) ?: nullError(this::rank)

		flags = fromBits(row.getInt("flags") ?: nullError(this::flags))
	}
}