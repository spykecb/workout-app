package jp.spykeh.workoutapp.data

import java.io.Serializable
import java.math.BigInteger
import java.util.*

data class UserRoutine(
    val id: Long,
    val user_id: Long,
    val routine_id: Long,
    val starting_date: Date,
    val current_week_no: Int,
    val current_day_no: Int,
    val completion_date: Date?,
    val routine: Routine,
    val workouts: List<Workout>?
) : Serializable
