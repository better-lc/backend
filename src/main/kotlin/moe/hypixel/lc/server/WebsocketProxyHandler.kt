package moe.hypixel.lc.server

import io.ktor.websocket.*
import io.netty.buffer.Unpooled
import io.netty.util.CharsetUtil.encoder
import moe.hypixel.lc.cache.AsyncRedis
import moe.hypixel.lc.cache.Cache
import moe.hypixel.lc.cosmetics.CosmeticManager
import moe.hypixel.lc.database.Database
import moe.hypixel.lc.server.packets.GiveCosmeticsPacket
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.sendPacket
import moe.hypixel.lc.utils.playerId
import moe.hypixel.lc.utils.players
import moe.hypixel.lc.utils.sockets
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.util.*

abstract class WebsocketProxyHandler(override val di: DI): DIAware {
	val cache by instance<Cache>()
	val db by instance<Database>()
	val redis by instance<AsyncRedis>()
	val cosmeticManager by instance<CosmeticManager>()

	lateinit var proxy: WebsocketProxy

	val playerId: UUID
		get() = socket.playerId

	val sockets: MutableSet<WebSocketServerSession>
		get() = proxy.clientSocket.sockets

	val socket: WebSocketServerSession
		get() = proxy.clientSocket

	fun cancelPacket(packet: Packet) {
		proxy.cancelPacket(packet)
	}

	suspend fun broadcastCosmeticChange(packet: GiveCosmeticsPacket) {
		val buf = Unpooled.buffer()
		packet.setDI(di)
		packet.write(buf)
		redis.broadcast("cosmetics", Base64.getEncoder().encodeToString(buf.array()))
		buf.release()

		for(socket in sockets.filter { it.players.contains(packet.id) }.filter { it != socket }) {
			socket.sendPacket(di, packet)
		}
	}

	abstract suspend fun onClientSend(packet: Packet)
	abstract suspend fun onServerSend(packet: Packet)
	abstract suspend fun onOpen()
}