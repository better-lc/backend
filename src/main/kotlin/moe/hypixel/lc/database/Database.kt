package moe.hypixel.lc.database

import moe.hypixel.lc.database.repos.UserRepo

class Database(
	connection: DatabaseConnection
) {
	val userRepo = UserRepo(connection)
}