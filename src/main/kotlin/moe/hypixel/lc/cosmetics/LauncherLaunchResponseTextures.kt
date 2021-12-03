package moe.hypixel.lc.cosmetics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LauncherLaunchResponseTextures(
	@SerialName("indexUrl")
	var indexUrl: String,

	@SerialName("baseUrl")
	var baseUrl: String
)
