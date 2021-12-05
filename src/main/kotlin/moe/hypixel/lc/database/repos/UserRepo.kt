package moe.hypixel.lc.database.repos

import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import moe.hypixel.lc.database.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import java.util.*

class UserRepo(
	val database: CoroutineDatabase
) {
	val collection = database.getCollection<User>("users")

	suspend fun get(id: UUID): User? = collection.findOne(User::uuid eq id)
	suspend fun save(user: User): UpdateResult {
		// validate cosmetics
		return collection.updateOneById(user.id, user)
	}

	suspend fun create(user: User): InsertOneResult {
		return collection.insertOne(user)
	}
}