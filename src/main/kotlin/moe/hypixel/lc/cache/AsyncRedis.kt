package moe.hypixel.lc.cache

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.future.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromStringMap

class AsyncRedis(
	redis: StatefulRedisConnection<String, String>
) {
	val client = redis.async()

	suspend fun get(key: String): String? {
		return client.get(key).await()
	}

	suspend fun set(key: String, value: String): String {
		return client.set(key, value).await()
	}

	suspend fun keys(pattern: String): List<String> {
		return client.keys(pattern).await()
	}

	@OptIn(ExperimentalSerializationApi::class)
	suspend inline fun <reified T: Any> getObject(key: String): T {
		return Properties.decodeFromStringMap(client.hgetall(key).await())
	}

	suspend fun delete(key: String): Boolean {
		return client.del(key).await() == 1L
	}

	suspend fun broadcast(channel: String, data: String): Long {
		return client.publish(channel, data).await()
	}

	companion object {
		fun create(uri: String): AsyncRedis {
			return create(RedisURI.create(uri))
		}

		fun create(uri: RedisURI): AsyncRedis {
			return create(RedisClient.create(uri))
		}

		fun create(redis: RedisClient): AsyncRedis {
			return AsyncRedis(redis.connect())
		}
	}
}