package moe.hypixel.lc.cache

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import moe.hypixel.lc.utils.asString
import java.util.*

class Cache(val redis: AsyncRedis) {
}
