package jp.spykeh.workoutapp.data

import com.google.gson.annotations.SerializedName
import jp.spykeh.workoutapp.adapter.EditExerciseDayAdapter
import java.io.Serializable

data class Routine (
    var id: Long = 0,
    var title: String,
    var weeks: Int,
    @SerializedName("exercises")
    var exerciseSelections: MutableList<ExerciseSelection>
) : Serializable {
    fun getExerciseDays() : MutableList<ExerciseDay>{
        val exerciseDaysHash : MutableMap<Int, ExerciseDay> = mutableMapOf()
        for(exerciseSelection in exerciseSelections){
            val dayNo = exerciseSelection.day_no
            if(!exerciseDaysHash.contains(dayNo)){
                exerciseDaysHash[dayNo] = ExerciseDay(dayNo, mutableListOf())
            }
            exerciseDaysHash[dayNo]?.exerciseSelections?.add(exerciseSelection)
        }
        return exerciseDaysHash.toList().map{it.second}.toMutableList()
    }
}