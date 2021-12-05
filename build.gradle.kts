val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val jasync_version: String by project
val kodein_version: String by project

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
	implementation("io.ktor:ktor-client-websockets:$ktor_version")
	implementation("io.ktor:ktor-client-core:$ktor_version")
	implementation("io.ktor:ktor-client-apache:$ktor_version")
	implementation("io.ktor:ktor-client-apache:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")
	implementation("io.ktor:ktor-server-netty:$ktor_version")
	implementation("io.ktor:ktor-client-serialization:$ktor_version")
	implementation("org.kodein.di:kodein-di:$kodein_version")
	implementation("org.kodein.di:kodein-di-jvm:$kodein_version")
	implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodein_version")
	implementation("ch.qos.logback:logback-classic:$logback_version")
//	implementation("com.github.jasync-sql:jasync-mysql:$jasync_version")
	implementation("org.mongodb:mongodb-driver-reactivestreams:4.4.0")
	implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.4.0")
	implementation("io.lettuce:lettuce-core:6.1.5.RELEASE")
	implementation("com.eatthepath:fast-uuid:0.2.0")
	//! If this breaks check version numbers and try remove "-RC"
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlin_version-RC")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.3.1")
	implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0")
}