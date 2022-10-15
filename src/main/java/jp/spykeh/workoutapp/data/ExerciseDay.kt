package jp.spykeh.workoutapp.data

import java.io.Serializable

data class ExerciseDay(
    var day: Int,
    var exerciseSelections: MutableList<ExerciseSelection>
) : Serializable
