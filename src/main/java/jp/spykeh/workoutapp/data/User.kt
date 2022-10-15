package jp.spykeh.workoutapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("user_id")
    var id: Long,
    var token: String,
    var name: String?,
    var email: String?
) : Serializable
