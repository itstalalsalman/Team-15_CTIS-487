package com.example.team_15_ctis_487.json

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl("https://www.jsonkeeper.com/b/")
                .addConverterFactory(GsonConverterFactory.create()) //retrofit will understand as a converter GSON converter will be used
                .build()

        return retrofit as Retrofit
    }
}