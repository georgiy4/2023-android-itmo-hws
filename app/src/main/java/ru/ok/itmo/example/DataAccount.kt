package ru.ok.itmo.example

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DataAccount(
    @SerializedName("name") val name: String,
    @SerializedName("pwd") val pwd: String
)