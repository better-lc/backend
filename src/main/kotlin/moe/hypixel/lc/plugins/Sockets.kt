package moe.hypixel.lc.plugins

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*
import io.netty.buffer.Unpooled
import moe.hypixel.lc.server.packets.out.GiveCosmeticsPacket
import moe.hypixel.lc.server.packets.out.Packet57
import moe.hypixel.lc.server.packets.out.StaffModulePacket
import moe.hypixel.lc.server.packets.utils.sendPacket

fun Application.configureSockets() {
	install(WebSockets) {
		pingPeriod = Duration.ofSeconds(15)
		timeout = Duration.ofSeconds(15)
		maxFrameSize = Long.MAX_VALUE
		masking = false
	}

	routing {
		webSocket("/") {
			println("Connection!")

			sendPacket(Packet57())
			sendPacket(GiveCosmeticsPacket())

			for (frame in incoming) {
				when (frame) {
					is Frame.Binary -> {
						val buf = Unpooled.copiedBuffer(frame.data)

					}
					else -> {
						//frame.toString()
					}
				}
			}
		}
	}
}