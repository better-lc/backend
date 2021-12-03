package moe.hypixel.lc.cosmetics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LauncherLaunchRequest(
	@SerialName("hwid")
	var hardwareId: String,

	@SerialName("os")
	var os: String,

	@SerialName("arch")
	var arch: String,

	@SerialName("launcher_version")
	var launcherVersion: String,

	@SerialName("version")
	var version: String,

	@SerialName("branch")
	var branch: String,

	@SerialName("launch_type")
	var launchType: String,

	@SerialName("classifier")
	var classifier: String
)
