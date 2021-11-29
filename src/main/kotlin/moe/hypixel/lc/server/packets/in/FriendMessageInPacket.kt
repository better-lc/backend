package moe.hypixel.lc.server.packets.`in`

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.InPacket
import moe.hypixel.lc.server.packets.PacketId
import moe.hypixel.lc.server.packets.utils.readString
import kotlin.properties.Delegates
import kotlin.properties.Delegates.notNull

@PacketId(5)
class FriendMessageInPacket: InPacket {
	var playerId by notNull<String>()
	var message by notNull<String>()

	override fun read(buf: ByteBuf) {
		playerId = buf.readString(52)
		message = buf.readString(1024)
	}

	override fun toString(): String {
		return "FriendMessageInPacket{playerId=$playerId,message=$message}"
	}
}