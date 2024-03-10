package ru.ok.itmo.example

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.ResponseBody

interface Api {

    companion object {
        const val BASE_URL = "https://faerytea.name:8008"

        private val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(Api::class.java)
    }
    @POST("/login")
    suspend fun login(@Body userRequest: DataAccount): ResponseBody

    @POST("/logout")
    suspend fun logout(@Header("X-Auth-Token") token: String)

    @GET("/channels")
    suspend fun getAllChannels(): Array<String>

    @GET("/channel/{channelName}")
    suspend fun getChannelMessages(@Path("channelName") name: String): List<Message>

    @POST("/messages")
    suspend fun message(@Header("X-Auth-Token") token: String, @Body message: Message): ResponseBody
}