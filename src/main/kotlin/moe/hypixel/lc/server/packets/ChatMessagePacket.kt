package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.writeString

@PacketId(65)
class ChatMessagePacket(
	var message: String
): Packet {
	constructor(): this("")
	override fun write(buf: ByteBuf) {
		buf.writeString(message)
	}

	override fun read(buf: ByteBuf) {
		message = buf.readString(512)
	}
}