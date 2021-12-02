package moe.hypixel.lc.database

import com.github.jasync.sql.db.RowData

interface DatabaseDeserializable {
	fun deserialize(row: RowData)
}