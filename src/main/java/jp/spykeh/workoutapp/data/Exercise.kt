package jp.spykeh.workoutapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Exercise(
    var id: Long? = 0,
    var title: String?,
    @SerializedName("muscle_groups")
    var muscleGroups: MutableList<MuscleGroup>?
) : Serializable
