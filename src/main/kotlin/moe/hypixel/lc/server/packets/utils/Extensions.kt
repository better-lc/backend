package moe.hypixel.lc.server.packets.utils

import io.ktor.http.cio.websocket.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kotlinx.coroutines.delay
import moe.hypixel.lc.server.packets.OutPacket
import moe.hypixel.lc.server.packets.Packet
import moe.hypixel.lc.server.packets.PacketId
import moe.hypixel.lc.server.packets.out.GiveCosmeticsPacket
import moe.hypixel.lc.server.packets.out.Packet57
import moe.hypixel.lc.server.packets.out.StaffModulePacket
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun Packet.getId() = javaClass.kotlin.getId()
fun KClass<Packet>.getId() = findAnnotation<PacketId>()!!.id

suspend fun WebSocketSession.sendPacket(outPacket: OutPacket) {
	val buf = Unpooled.buffer()

	buf.writeVarInt(outPacket.getId())

	outPacket.write(buf)

	send(buf.array())
}

fun ByteBuf.writeVarInt(input: Int) = ByteBufHelper.writeVarInt(this,input)
fun ByteBuf.readVarInt() = ByteBufHelper.readVarInt(this)
fun ByteBuf.readString(maxLength: Int): String = ByteBufHelper.readString(this, maxLength)
fun ByteBuf.writeString(string: String) = ByteBufHelper.writeString(this, string)
