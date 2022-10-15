package jp.spykeh.workoutapp.data

import java.io.Serializable

data class MuscleGroup(
    var id: Int? = 0,
    var title: String,
    var score: Float = 0f
) : Serializable
