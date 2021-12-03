package moe.hypixel.lc.database

import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder
import org.intellij.lang.annotations.Language
import kotlin.reflect.KClass
import kotlin.reflect.full.*

class DatabaseConnection(
	val connection: SuspendingConnection
) {
	constructor(connectionString: String) : this(MySQLConnectionBuilder.createConnectionPool(connectionString) {
		maxActiveConnections = 20
	}.asSuspending)

	suspend fun execute(@Language("SQL") query: String, args: List<Any?> = listOf()): QueryResult = connection.sendPreparedStatement(query, args)

	suspend fun <T: DatabaseDeserializable> query(@Language("SQL") query: String, clazz: KClass<T>, args: List<Any?> = listOf()): List<T> = execute(query, args).rows.mapNotNull { clazz.createInstance().apply { deserialize(it) } }
	suspend fun <T: DatabaseDeserializable> query(@Language("SQL") query: String, clazz: KClass<T>, vararg args: Any?) = query(query, clazz, listOf(*args))

	suspend fun <T: DatabaseDeserializable> querySingle(@Language("SQL") query: String, clazz: KClass<T>, args: List<Any?> = listOf()): T? = query(query, clazz, args).firstOrNull()
	suspend fun <T: DatabaseDeserializable> querySingle(@Language("SQL") query: String, clazz: KClass<T>, vararg args: Any?): T? = query(query, clazz, args).firstOrNull()

	suspend inline fun <reified T: DatabaseDeserializable> query(@Language("SQL") query: String, args: List<Any?> = listOf()): List<T> = query(query, T::class, args)
	suspend inline fun <reified T: DatabaseDeserializable> query(@Language("SQL") query: String, vararg args: Any?): List<T> = query(query, T::class, args)

	suspend inline fun <reified T : DatabaseDeserializable> querySingle(@Language("SQL") query: String, args: List<Any?> = listOf()): T? = querySingle(query, T::class, args)
	suspend inline fun <reified T : DatabaseDeserializable> querySingle(@Language("SQL") query: String, vararg args: Any?): T? = querySingle(query, T::class, args)
}