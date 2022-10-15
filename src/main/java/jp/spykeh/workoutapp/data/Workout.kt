package jp.spykeh.workoutapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Workout(
    var id: Long? = 0,
    @SerializedName("completion_date")
    var completionDate: Date?,
    @SerializedName("week_no")
    var weekNo: Int,
    @SerializedName("day_no")
    var dayNo: Int,
    @SerializedName("routine_id")
    var routineId: Long?,
    var exercises : MutableList<WorkoutExercise>
) : Serializable{
    fun addExercise(exercise: WorkoutExercise){
        exercises.add(exercise)
    }

    fun setExerciseReps(exerciseId : Long, set: Int, reps: Int){
        for(exercise in exercises){
            if(exercise.exerciseSelection.id == exerciseId){
                exercise.setReps(set, reps)
            }
        }
    }

    fun setExerciseWeight(exerciseId : Long, set: Int, weight: Float){
        for(exercise in exercises){
            if(exercise.exerciseSelection.id == exerciseId){
                exercise.setWeight(set, weight)
            }
        }
    }

    fun setExerciseNote(exerciseId: Long, note: String){
        for(exercise in exercises){
            if(exercise.exerciseSelection.id == exerciseId){
                exercise.setNotes(note)
            }
        }
    }
}

