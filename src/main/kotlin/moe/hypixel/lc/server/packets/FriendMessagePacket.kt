package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.writeString
import javax.management.monitor.StringMonitor
import kotlin.properties.Delegates.notNull

@PacketId(5)
class FriendMessagePacket(
	var playerId: String,
	var message: String
): Packet {
	constructor(): this("", "")

	override fun write(buf: ByteBuf) {
		buf.writeString(playerId)
		buf.writeString(message)
	}

	override fun read(buf: ByteBuf) {
		playerId = buf.readString(52)
		message = buf.readString(1024)
	}

	override fun toString(): String {
		return "FriendMessageInPacket{playerId=$playerId,message=$message}"
	}
}