package moe.hypixel.lc.server.packets.utils

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import moe.hypixel.lc.server.packets.DIPacket
import org.kodein.di.DI
import org.kodein.di.ktor.closestDI
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun Packet.getId() = javaClass.kotlin.getId()
fun KClass<Packet>.getId() = findAnnotation<PacketId>()!!.id

suspend fun DefaultWebSocketServerSession.sendPacket(packet: Packet) {
	sendPacket(closestDI { call.application }, packet)
}

suspend fun WebSocketSession.sendPacket(di: DI, packet: Packet) {
	val buf = Unpooled.buffer()

	buf.writeVarInt(packet.getId())

	if(packet is DIPacket)
		packet.setDI(di)

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