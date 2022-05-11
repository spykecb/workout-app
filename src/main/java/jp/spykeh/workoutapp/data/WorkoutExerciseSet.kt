package jp.spykeh.workoutapp.data

import java.io.Serializable

data class WorkoutExerciseSet(
    var set: Int,
    var weight: Float,
    var reps: Int,
) : Serializable {
    var isOptional = false
}
