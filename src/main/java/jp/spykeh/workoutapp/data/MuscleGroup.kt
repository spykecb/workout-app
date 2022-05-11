package jp.spykeh.workoutapp.data

import java.io.Serializable

data class MuscleGroup(
    var id: Int? = 0,
    var title: String
) : Serializable
