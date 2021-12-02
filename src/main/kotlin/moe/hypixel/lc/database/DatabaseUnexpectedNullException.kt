package moe.hypixel.lc.database

import kotlin.reflect.KMutableProperty0

class DatabaseUnexpectedNullException(prop: KMutableProperty0<*>) : DatabaseDeserializeException(prop, "Property ${prop.name} when it was not expected to be")