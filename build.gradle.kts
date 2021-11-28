val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	application
	java
	id("com.github.johnrengelman.shadow") version "7.1.0"
	kotlin("jvm") version "1.6.0"
	id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
}

group = "moe.hypixel.lc"
version = "0.0.1"
application {
	mainClass.set("moe.hypixel.lc.ApplicationKt")
}

repositories {
	mavenCentral()
}

sourceSets {
	main {
		java {
			srcDirs("src/main/kotlin")
		}
	}
}

dependencies {
	implementation("io.ktor:ktor-server-core:$ktor_version")
	implementation("io.ktor:ktor-serialization:$ktor_version")
	implementation("io.ktor:ktor-websockets:$ktor_version")
	implementation("io.ktor:ktor-server-netty:$ktor_version")
	implementation("ch.qos.logback:logback-classic:$logback_version")
	testImplementation("io.ktor:ktor-server-tests:$ktor_version")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}