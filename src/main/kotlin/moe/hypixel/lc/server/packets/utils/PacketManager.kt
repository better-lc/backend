package moe.hypixel.lc.server.packets.utils

import io.ktor.http.cio.websocket.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import moe.hypixel.lc.server.packets.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class PacketManager {
	private val inPackets = mutableSetOf<KClass<out Packet>>()

	fun getPacket(frame: Frame): Packet? {
		return getPacket(frame.data)
	}

	fun getPacket(data: ByteArray): Packet? {
		return getPacket(Unpooled.copiedBuffer(data))
	}

	fun getPacket(buf: ByteBuf): Packet? {
		val id = buf.readVarInt()
		val packet = getPacket(id)
		packet?.read(buf)
		buf.release()
		return packet
	}

	fun getPacket(id: Int): Packet? {
		return inPackets.firstOrNull {
			(it as KClass<Packet>).getId() == id || it.findAnnotation<AdditionalPacketIds>()?.ids?.contains(
				id
			) ?: false
		}?.createInstance()
	}

	fun registerInPacket(packetClass: KClass<out Packet>) {
		if(!packetClass.hasAnnotation<PacketId>())
			throw IllegalArgumentException("Packet ${packetClass.simpleName} does not have id")

		inPackets.add(packetClass)
	}

	fun registerPackets(vararg packetClasses: KClass<out Packet>) {
		for(packetClass in packetClasses) {
			registerInPacket(packetClass)
		}
	}

	companion object {
		fun createDefault(): PacketManager {
			val packetManager = PacketManager()
			packetManager.registerPackets(
				BanMessagePacket::class,
				ChatMessagePacket::class,
				ClientSettingsInPacket::class,
				CosmeticChangePacket::class,
				DoEmoteInPacket::class,
				DoEmoteOutPacket::class,
				EmoteGivePacket::class,
				FriendMessagePacket::class,
				//TODO: Implement
				//FriendsListPacket::class,
				GiveCosmeticsPacket::class,
				RemoveTrackedPlayersPacket::class,
				AddTrackedPlayersPacket::class,
				ServerDataInPacket::class
			)
			return packetManager
		}
	}
}