package moe.hypixel.lc.server.packets

import io.netty.buffer.ByteBuf
import jdk.jfr.Enabled
import moe.hypixel.lc.server.objects.OfflinePlayer
import moe.hypixel.lc.server.objects.Player
import moe.hypixel.lc.server.packets.utils.Packet
import moe.hypixel.lc.server.packets.utils.PacketId
import moe.hypixel.lc.server.packets.utils.readString
import moe.hypixel.lc.server.packets.utils.writeString

@PacketId(4)
class FriendsListPacket(
	var consoleAccess: Boolean,
	var requestsEnabled: Boolean,
	var onlineFriends: MutableList<Player>,
	var offlineFriends: MutableList<OfflinePlayer>
): Packet {
	constructor(): this(true, true, mutableListOf(), mutableListOf())

	override fun write(buf: ByteBuf) {
		buf.writeBoolean(consoleAccess)
		buf.writeBoolean(requestsEnabled)
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
			buf.writeLong(offlineFriend.status.toLong())
		}
	}

	override fun read(buf: ByteBuf) {
		consoleAccess = buf.readBoolean()
		requestsEnabled = buf.readBoolean()
		val onlineLen = buf.readInt()
		val offlineLen = buf.readInt()

		for(i in 0 until onlineLen) {
			onlineFriends.add(
				Player(
					buf.readString(52),
					buf.readString(32),
					buf.readInt(),
					buf.readString(256)
				)
			)
		}

		for(i in 0 until offlineLen) {
			offlineFriends.add(
				OfflinePlayer(
					buf.readString(52),
					buf.readString(32),
					buf.readLong().toInt(),
					""
				)
			)
		}
	}
}