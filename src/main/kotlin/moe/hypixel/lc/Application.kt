package moe.hypixel.lc

import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import moe.hypixel.lc.cache.AsyncRedis
import moe.hypixel.lc.cache.Cache
import moe.hypixel.lc.cosmetics.CosmeticManager
import moe.hypixel.lc.database.Database
import moe.hypixel.lc.plugins.*
import moe.hypixel.lc.utils.instance
import moe.hypixel.lc.utils.websocketsAttribute
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun main() {
	embeddedServer(Netty, environment = applicationEngineEnvironment {
		config = MapApplicationConfig()

		module {
			di {
				bind<Database>() with singleton { Database.create(System.getenv("DATABASE_URI"), System.getenv("DATABASE_NAME")) }
				bind<AsyncRedis>() with singleton { AsyncRedis.create(System.getenv("REDIS_URI")) }

				bind<Cache>() with singleton {
					val redis by instance<AsyncRedis>()
					Cache(redis)
				}

				bind<CosmeticManager>() with singleton { runBlocking { CosmeticManager.create() } }
			}

			attributes.put(websocketsAttribute, mutableSetOf())

			configureRouting()
			configureSerialization()
			configureSockets()
		}

		connector {
			host = "0.0.0.0"
			port = System.getenv().getOrDefault("PORT", "3000").toInt()
		}
	}).start(wait = true)
}
