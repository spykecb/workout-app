package jp.spykeh.workoutapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WorkoutExercise(
    var exerciseSelection: ExerciseSelection,
    @SerializedName("exercise_selection_id")
    var exerciseSelectionId: Long?,
    @SerializedName("exercise_id")
    var exerciseId: Long?,
    var sets: MutableList<WorkoutExerciseSet>,
    var note: String?
) : Serializable {

    fun setReps(set:Int, reps: Int){
        sets[set - 1].reps = reps
    }

    fun setWeight(set:Int, weight: Float){
        sets[set - 1].weight = weight
    }

    fun setNotes(_note: String){
        note = _note
    }
}
