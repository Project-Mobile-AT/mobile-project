package com.example.myapplication.data

import com.example.myapplication.model.Usuario
import com.example.myapplication.utils.SUPABASE_API_KEY
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface SupabaseService {

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )

    @POST("rest/v1/usuario")
    suspend fun criarUsuario(@Body usuario: Usuario): List<Usuario>

}
