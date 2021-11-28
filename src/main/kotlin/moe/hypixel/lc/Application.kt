package moe.hypixel.lc

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import moe.hypixel.lc.plugins.*

fun main() {
	embeddedServer(Netty, port = System.getenv().getOrDefault("PORT", "3000").toInt(), host = "0.0.0.0") {
		configureRouting()
		configureSerialization()
		configureSockets()
	}.start(wait = true)
}
