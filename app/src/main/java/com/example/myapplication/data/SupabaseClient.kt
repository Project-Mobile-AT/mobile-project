package com.example.myapplication.data

import com.example.myapplication.utils.SUPABASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SUPABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: SupabaseService = retrofit.create(SupabaseService::class.java)
}
