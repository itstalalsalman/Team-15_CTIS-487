package com.example.team_15_ctis_487.json

import com.example.team_15_ctis_487.retrofitmodel.FitnessData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("DS4E")
    fun getParentJSONObject(): Call<FitnessData>
}