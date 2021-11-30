package moe.hypixel.lc.server.packets.`in`

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.InPacket
import moe.hypixel.lc.server.packets.PacketId
import moe.hypixel.lc.server.packets.utils.readString

@PacketId(6)
class ServerDataInPacket: InPacket {
	lateinit var someString: String
	lateinit var server: String

	override fun read(buf: ByteBuf) {
		someString = buf.readString(52)
		server = buf.readString(100)
	}
}