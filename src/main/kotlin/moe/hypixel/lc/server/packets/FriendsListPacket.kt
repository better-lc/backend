package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import moe.hypixel.lc.server.objects.OfflinePlayer
import moe.hypixel.lc.server.objects.Player
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.writeString

@PacketId(4)
class FriendsListPacket(
	var onlineFriends: MutableSet<Player>,
	var offlineFriends: MutableSet<OfflinePlayer>
): Packet {
	constructor(): this(mutableSetOf(), mutableSetOf())

	override fun write(buf: ByteBuf) {
		buf.writeBoolean(true)
		buf.writeBoolean(true)
		buf.writeInt(onlineFriends.size)
		buf.writeInt(offlineFriends.size)

		for(onlineFriend in onlineFriends) {
			buf.writeString(onlineFriend.id) // id
			buf.writeString(onlineFriend.username)
			buf.writeInt(onlineFriend.status)
			buf.writeString(onlineFriend.statusMessage)
		}

		for(offlineFriend in offlineFriends) {
			buf.writeString(offlineFriend.id)
			buf.writeString(offlineFriend.username)
			buf.writeInt(3)
		}
	}

	override fun read(buf: ByteBuf) {

	}
}