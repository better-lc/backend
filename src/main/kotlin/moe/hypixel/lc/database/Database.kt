package moe.hypixel.lc.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.database.repos.UserRepo
import org.bson.UuidRepresentation
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class Database(
	val client: CoroutineClient,
	dbName: String
) {
	val db = client.getDatabase(dbName)
	val userRepo = UserRepo(db)

	companion object {
		fun create(connectionString: String, dbName: String): Database {

			return Database(
				KMongo.createClient(
					MongoClientSettings
						.builder()
						.applyConnectionString(
							ConnectionString(
								connectionString
							)
						)
						.uuidRepresentation(UuidRepresentation.STANDARD)
						.build()
				).coroutine,
				dbName
			)
		}
	}
}