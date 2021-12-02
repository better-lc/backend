package moe.hypixel.lc.cache

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import moe.hypixel.lc.utils.asString
import java.util.*

class Cache(val redis: AsyncRedis) {
	suspend fun playerExists(id: UUID): Boolean {
		val idStr = id.asString()

		val redisData = redis.get("redis-player-cache:${idStr}")
		if(redisData != null) {
			return redisData == "true"
		} else {
			val client = HttpClient(CIO)
			val res = client.request<HttpResponse>("https://sessionserver.mojang.com/session/minecraft/profile/${idStr}")
			if(res.status == HttpStatusCode.OK) {
				redis.set("redis-player-cache:${idStr}", "true")
				return true
			} else if(res.status == HttpStatusCode.NoContent) {
				redis.set("redis-player-cache:${idStr}", "false")
				return false
			}

			return false
		}
	}
}
