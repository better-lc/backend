package moe.hypixel.lc.utils

import com.eatthepath.uuid.FastUUID
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.websocket.*
import moe.hypixel.lc.cosmetics.Cosmetic
import moe.hypixel.lc.cosmetics.CosmeticType
import moe.hypixel.lc.server.objects.OfflinePlayer
import moe.hypixel.lc.server.objects.Player
import org.kodein.di.DIProperty
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.*

inline fun <reified T: Any> Application.instance(): DIProperty<T> {
	return closestDI().instance()
}

inline fun <reified T: Any> Routing.instance(): DIProperty<T> = application.instance()
inline fun <reified T: Any> DefaultWebSocketServerSession.instance(): DIProperty<T> = application.instance()

fun UUID.asString(): String {
	return FastUUID.toString(this).replace("-", "")
}

fun String.dashUUID() = replaceFirst(Regex("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)" ), "$1-$2-$3-$4-$5")

val playerIdAttribute = AttributeKey<UUID>("playerId")

var WebSocketServerSession.playerId: UUID
	get() = call.attributes[playerIdAttribute]
	set(value) = call.attributes.put(playerIdAttribute, value)

val websocketsAttribute = AttributeKey<MutableSet<WebSocketServerSession>>("websockets")

val WebSocketServerSession.sockets
	get() = application.attributes[websocketsAttribute]

val playersKey = AttributeKey<MutableList<UUID>>("players")

var WebSocketServerSession.trackedPlayers
	get() = call.attributes[playersKey]
	set(value) = call.attributes.put(playersKey, value)

operator fun Set<Cosmetic>.component1(): Cosmetic? {
	return firstOrNull { it.type == CosmeticType.CAPE }
}

operator fun Set<Cosmetic>.component2(): Cosmetic? {
	return firstOrNull { it.type == CosmeticType.HAT }
}

operator fun Set<Cosmetic>.component3(): Cosmetic? {
	return firstOrNull { it.type == CosmeticType.MASK }
}

operator fun Set<Cosmetic>.component4(): Cosmetic? {
	return firstOrNull { it.type == CosmeticType.BANDANNA }
}

operator fun Set<Cosmetic>.component5(): Cosmetic? {
	return firstOrNull { it.type == CosmeticType.TIE }
}

operator fun Set<Cosmetic>.component6(): Cosmetic? {
	return firstOrNull { it.type == CosmeticType.WINGS }
}

operator fun Set<Cosmetic>.component7(): Cosmetic? {
	return firstOrNull { it.type == CosmeticType.BACKPACK }
}

inline fun <reified T: OfflinePlayer> MutableList<T>.removeSystem(systemId: UUID) {
	val old = mutableListOf(*this.toTypedArray())
	clear()

	addAll(old.filter { FastUUID.parseUUID(it.id) != systemId })
}