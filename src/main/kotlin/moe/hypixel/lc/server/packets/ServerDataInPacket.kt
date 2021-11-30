package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.writeString

@PacketId(6)
class ServerDataInPacket(
	var someString: String,
	var server: String
): Packet {
	constructor(): this("", "")

	override fun write(buf: ByteBuf) {
		buf.writeString(someString)
		buf.writeString(server)
	}

	override fun read(buf: ByteBuf) {
		someString = buf.readString(52)
		server = buf.readString(100)
	}
}