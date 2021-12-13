package moe.hypixel.lc.server.packets.utils

import io.ktor.http.cio.websocket.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import moe.hypixel.lc.server.packets.*
import org.kodein.di.DI
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class PacketManager(
	val di: DI
) {
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

		if(packet == null) {
			//println("Received packet with id $id")
		}
		packet?.read(buf)
		buf.release()
		return packet
	}

	fun getPacket(id: Int): Packet? {
		val packet = inPackets.firstOrNull {
			(it as KClass<Packet>).getId() == id || it.findAnnotation<AdditionalPacketIds>()?.ids?.contains(
				id
			) ?: false
		}?.createInstance()

		if(packet is DIPacket) {
			packet.setDI(di)
		}

		return packet
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
		fun createDefault(di: DI): PacketManager {
			val packetManager = PacketManager(di)

			packetManager.registerPackets(
				BanMessagePacket::class,
				ChatMessagePacket::class,
				ClientSettingsInPacket::class,
				CosmeticChangePacket::class,
				DoEmoteInPacket::class,
				DoEmoteOutPacket::class,
				GiveEmotesPacket::class,
				FriendMessagePacket::class,
				FriendsListPacket::class,
				GiveCosmeticsPacket::class,
				RemoveTrackedPlayersPacket::class,
				AddTrackedPlayersPacket::class,
				ServerDataInPacket::class,
				EquipEmotesPacket::class,
				CrashPacket::class
			)

			return packetManager
		}
	}
}