package jp.spykeh.workoutapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WorkoutExerciseSet(
    @SerializedName("set_no")
    var set: Int,
    var weight: Float,
    var reps: Int,
) : Serializable {
    var isOptional = false
}
