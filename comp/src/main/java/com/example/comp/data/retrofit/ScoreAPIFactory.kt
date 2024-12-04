package com.example.comp.data.retrofit

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//TODO does this even need to be inject?
class ScoreAPIFactory @Inject constructor() {
    private var currentBaseURL: String? = null

    //TODO how to make initial setup lazy? by delegating?
    private val _api: MutableStateFlow<ScoreAPI?> = MutableStateFlow(null)
    val api = _api.asStateFlow()

    @Synchronized
    fun setURL(url: String) {
        currentBaseURL = url
        _api.value = createScoreAPI()
    }

    fun createScoreAPI(): ScoreAPI? {
        if(currentBaseURL == null ) {
            return null
        } //TODO do this some other way
        return Retrofit.Builder()
            .baseUrl(currentBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build())
            .build()
            .create(ScoreAPI::class.java)
    }
}