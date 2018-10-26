package com.studio.minhnt.congnghegiaoduc.api

import android.arch.lifecycle.LiveData
import com.studio.minhnt.congnghegiaoduc.model.Voice
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AppService {
    @POST("text2speech/v4")
    fun getVoice(@Header("voice") voice: String, @Body content: String): LiveData<ApiResponse<Voice>>
}