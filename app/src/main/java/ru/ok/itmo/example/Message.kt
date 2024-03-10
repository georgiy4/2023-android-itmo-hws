package ru.ok.itmo.example

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TextData(
    @SerializedName("text") val text: String
)

@Serializable
data class ImageData(
    @SerializedName("link") val link: String?
)

@Serializable
data class MessageData(
    @SerializedName("Text") val text: TextData? = null,
    @SerializedName("Image") val image: ImageData? = null,
)

@Serializable
data class Message(
    @SerializedName("id") val id: Long,
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String?,
    @SerializedName("data") val data: MessageData,
    @SerializedName("time") val time: Long? = null,
)