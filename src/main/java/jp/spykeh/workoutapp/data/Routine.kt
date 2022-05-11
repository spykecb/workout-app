package jp.spykeh.workoutapp.data

import java.io.Serializable

data class Routine (
    var id: Long = 0,
    var title: String,
    var weeks: Int,
    var exerciseDays: MutableList<ExerciseDay>,
) : Serializable