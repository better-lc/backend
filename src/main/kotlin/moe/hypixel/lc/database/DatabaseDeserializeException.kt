package moe.hypixel.lc.database

import kotlin.reflect.KMutableProperty0

open class DatabaseDeserializeException(val property: KMutableProperty0<*>, message: String? = null) : Exception(message ?: "Property ${property.name} failed to deserialize")