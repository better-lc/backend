package moe.hypixel.lc.server.packets.`in`

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.InPacket
import moe.hypixel.lc.server.packets.PacketId
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.readVarInt

@PacketId(64)
class ClientSettingsInPacket: InPacket {
	var data = mutableListOf<Pair<String, Boolean>>()
	lateinit var msg: String

	override fun read(buf: ByteBuf) {
		val length = buf.readVarInt()

		for(i in 0 until length) {
			val str = buf.readString(500)
			val bool = buf.readBoolean()
			data.add(str to bool)
		}

		msg = buf.readString(500)
	}
}