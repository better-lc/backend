package moe.hypixel.lc.cosmetics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LauncherLaunchResponse(
	@SerialName("textures")
	val textures: LauncherLaunchResponseTextures
)