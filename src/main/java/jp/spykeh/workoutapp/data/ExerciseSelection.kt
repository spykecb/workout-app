package jp.spykeh.workoutapp.data

import java.io.Serializable

data class ExerciseSelection(
    var id: Long? = 0,
    var order_no: Int = 0,
    var exercise: Exercise,
    var sets: Pair<Int, Int>,
    var reps: String,
) : Serializable
