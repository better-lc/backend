package moe.hypixel.lc.server.packets.utils

import io.ktor.http.cio.websocket.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun Packet.getId() = javaClass.kotlin.getId()
fun KClass<Packet>.getId() = findAnnotation<PacketId>()!!.id

suspend fun WebSocketSession.sendPacket(packet: Packet) {
	val buf = Unpooled.buffer()

	buf.writeVarInt(packet.getId())

	packet.write(buf)

	send(buf.array())
	buf.release()
}

fun ByteBuf.writeVarInt(input: Int) = ByteBufHelper.writeVarInt(this,input)
fun ByteBuf.readVarInt() = ByteBufHelper.readVarInt(this)
fun ByteBuf.readString(maxLength: Int): String = ByteBufHelper.readString(this, maxLength)
fun ByteBuf.writeString(string: String) = ByteBufHelper.writeString(this, string)
fun ByteBuf.writeVarIntList(list: List<Int>) {
	writeVarInt(list.size)
	for(elem in list) writeVarInt(elem)
}