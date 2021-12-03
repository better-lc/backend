package moe.hypixel.lc.database.repos

import com.github.jasync.sql.db.QueryResult
import moe.hypixel.lc.cosmetics.Cosmetic
import moe.hypixel.lc.database.DatabaseConnection
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.utils.*
import java.util.*

class UserRepo(
	val connection: DatabaseConnection
) {
	suspend fun getUser(id: UUID) = connection.querySingle<User>("INSERT INTO users (id) VALUES (?) ON DUPLICATE KEY UPDATE id=id RETURNING *;", listOf(id.asString()))!!

	suspend fun setUserCosmetics(id: UUID, cosmetics: Set<Cosmetic>): QueryResult {
		val (
			cape,
			hat,
			mask,
			bandanna,
			tie,
			wings,
			backpack
		) = cosmetics

		return connection.execute("""
			UPDATE users SET
				cape_id=?,
				hat_id=?,
				mask_id=?,
				bandanna_id=?,
				tie_id=?,
				wings_id=?,
				backpack_id=?
			WHERE id=?;
		""".trimIndent(), listOf(
			cape?.id,
			hat?.id,
			mask?.id,
			bandanna?.id,
			tie?.id,
			wings?.id,
			backpack?.id,
			id.asString(),
		))
	}
}