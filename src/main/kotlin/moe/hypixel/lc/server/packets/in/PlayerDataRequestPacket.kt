package moe.hypixel.lc.server.packets.`in`

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.packets.AdditionalPacketIds
import moe.hypixel.lc.server.packets.InPacket
import moe.hypixel.lc.server.packets.PacketId
import moe.hypixel.lc.server.packets.utils.readVarInt
import java.util.*

@PacketId(50)
@AdditionalPacketIds(48)
class PlayerDataRequestPacket: InPacket {
	var requestedPlayers = mutableListOf<UUID>()
	override fun read(buf: ByteBuf) {
		for(i in 0 until buf.readVarInt()) {
			requestedPlayers.add(UUID(buf.readLong(), buf.readLong()))
		}
	}
}