package jp.spykeh.workoutapp.data

import java.util.*

data class TableSync(
    val table_name: String,
    var last_update_date: Date?
)
