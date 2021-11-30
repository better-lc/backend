package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import java.util.*

@PacketId(51)
class DoEmoteOutPacket(
	var playerId: UUID,
	var id: Int
): Packet {
	constructor(): this(UUID.randomUUID(), -1)

	override fun write(buf: ByteBuf) {
		buf.writeLong(playerId.mostSignificantBits)
		buf.writeLong(playerId.leastSignificantBits)
		buf.writeInt(id)
	}

	override fun read(buf: ByteBuf) {
		playerId = UUID(buf.readLong(), buf.readLong())
		id = buf.readInt()
	}
}