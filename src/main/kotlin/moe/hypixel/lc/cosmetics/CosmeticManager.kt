package moe.hypixel.lc.cosmetics

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import moe.hypixel.lc.database.models.User
import moe.hypixel.lc.database.models.user.UserRank
import moe.hypixel.lc.server.packets.obj.PlayerCosmetic

class CosmeticManager {
	private var cosmetics = mutableSetOf<Cosmetic>()

	suspend fun init() {
		val client = HttpClient(CIO) {
			install(JsonFeature) {
				serializer = KotlinxSerializer(Json {
					ignoreUnknownKeys = true
				})
			}
		}

		val launchResponse = client.post<HttpResponse>("https://api.lunarclientprod.com/launcher/launch") {
			contentType(ContentType.Application.Json)
			body = LauncherLaunchRequest(
				"",
				"win32",
				"x64",
				"2.9.1",
				"1.8",
				"master",
				"ONLINE",
				""
			)
		}

		val launchResponseData = launchResponse.receive<LauncherLaunchResponse>()

		val indexResponse = client.get<HttpResponse>(launchResponseData.textures.indexUrl)

		val indexResponseBody = indexResponse.receive<String>()

		val indexHash = indexResponseBody
			.split("\n")
			.map { it.split(" ") }
			.first { it[0] == "assets/lunar/cosmetics/index" }[1]


		val indexFile = client.get<HttpResponse>(launchResponseData.textures.baseUrl + indexHash)
		val indexFileBody = indexFile.receive<String>()

		parseIndex(indexFileBody)
	}

	fun parseIndex(indexFileBody: String) {
		cosmetics.clear()

		val parsedIndex = csvReader().readAll(indexFileBody)
		for(cosmeticData in parsedIndex.filter { it[2] != "EMPTY" }) {
			cosmetics.add(Cosmetic(
				cosmeticData[0].toInt(), // id
				cosmeticData[1].toFloat(), // some float
				cosmeticData[2], // texture
				cosmeticData[3], // name
				CosmeticType.fromString(cosmeticData[4], cosmeticData[6]), // type
				cosmeticData[5].toBoolean(), // who fucking knows
				cosmeticData[7], // not a clue
				cosmeticData[8] // other texture?
			))
		}
	}

	fun getCosmetic(id: Int) = cosmetics.firstOrNull { it.id == id }

	fun getCosmetics(): Set<Cosmetic> {
		return cosmetics
	}

	companion object {
		suspend fun create(): CosmeticManager {
			return CosmeticManager().apply { init() }
		}
	}
}