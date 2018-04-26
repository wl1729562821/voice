package com.iwith.voice.data.api

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

internal interface HttpApi{

    @POST("/coron-nlu/nlu")
    fun getMachineLanguage(@Body response:RequestBody):Call<JsonObject>

}