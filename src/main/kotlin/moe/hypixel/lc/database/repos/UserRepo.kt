package moe.hypixel.lc.database.repos

import moe.hypixel.lc.database.DatabaseConnection
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.utils.asString
import moe.hypixel.lc.utils.dashUUID
import java.util.*

class UserRepo(
	val connection: DatabaseConnection
) {
	suspend fun getUser(id: UUID) = connection.querySingle<User>("INSERT INTO users (id) VALUES (?) ON DUPLICATE KEY UPDATE id=id RETURNING *;", listOf(id.asString()))!!
}