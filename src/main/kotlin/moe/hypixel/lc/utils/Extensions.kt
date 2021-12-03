package moe.hypixel.lc.utils

import com.eatthepath.uuid.FastUUID
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.websocket.*
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