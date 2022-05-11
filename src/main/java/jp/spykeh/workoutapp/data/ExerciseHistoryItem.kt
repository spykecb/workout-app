package jp.spykeh.workoutapp.data

import java.io.Serializable
import java.util.*

data class ExerciseHistoryItem(
    val completionDate: Date,
    val sets: ArrayList<WorkoutExerciseSet>
) : Serializable{
    fun getSetsString() : String{
        var str = ""
        for (set in sets){
            str += "${set.reps} reps x ${set.weight} kg\n"
        }
        return str
    }
}
