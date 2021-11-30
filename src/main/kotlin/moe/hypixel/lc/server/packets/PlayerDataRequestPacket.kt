package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.utils.*
import java.util.*

@PacketId(50)
@AdditionalPacketIds(48)
class PlayerDataRequestPacket(
	var requestedPlayers: MutableList<UUID>
): Packet {
	constructor(): this(mutableListOf())

	override fun write(buf: ByteBuf) {
		buf.writeVarInt(requestedPlayers.size)
		for(requestedPlayer in requestedPlayers) {
			buf.writeLong(requestedPlayer.mostSignificantBits)
			buf.writeLong(requestedPlayer.leastSignificantBits)
		}
	}

	override fun read(buf: ByteBuf) {
		for(i in 0 until buf.readVarInt()) {
			requestedPlayers.add(UUID(buf.readLong(), buf.readLong()))
		}
	}
}