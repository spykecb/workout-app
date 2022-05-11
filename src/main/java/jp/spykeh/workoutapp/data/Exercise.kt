package jp.spykeh.workoutapp.data

import java.io.Serializable

data class Exercise(
    var id: Long? = 0,
    var title: String,
    var priMuscleGroup: MuscleGroup
) : Serializable
