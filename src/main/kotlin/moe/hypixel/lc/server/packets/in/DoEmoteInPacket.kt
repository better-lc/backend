package moe.hypixel.lc.server.packets.`in`

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.InPacket
import moe.hypixel.lc.server.packets.PacketId
import kotlin.properties.Delegates
import kotlin.properties.Delegates.notNull

@PacketId(39)
class DoEmoteInPacket: InPacket {
	var emoteId by notNull<Int>()

	override fun read(buf: ByteBuf) {
		emoteId = buf.readInt()
	}
}