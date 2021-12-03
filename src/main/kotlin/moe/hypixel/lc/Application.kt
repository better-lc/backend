package moe.hypixel.lc

import io.ktor.config.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import moe.hypixel.lc.cache.AsyncRedis
import moe.hypixel.lc.cache.Cache
import moe.hypixel.lc.cosmetics.Cosmetic
import moe.hypixel.lc.cosmetics.CosmeticManager
import moe.hypixel.lc.database.Database
import moe.hypixel.lc.database.DatabaseConnection
import moe.hypixel.lc.plugins.*
import moe.hypixel.lc.utils.instance
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.KeyStore
import javax.annotation.processing.ProcessingEnvironment

fun main() {
	embeddedServer(Netty, environment = applicationEngineEnvironment {
		config = MapApplicationConfig()

		module {
			di {
				bind<Database>() with singleton { Database(DatabaseConnection(System.getenv("DATABASE_URI"))) }
				bind<Cache>() with singleton { Cache(AsyncRedis.create(System.getenv("REDIS_URI"))) }
				bind<CosmeticManager>() with singleton { runBlocking { CosmeticManager.create() } }
			}

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