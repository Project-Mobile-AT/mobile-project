package com.example.myapplication.data

import com.example.myapplication.model.AtualizacaoInformativo
import com.example.myapplication.model.Informativo // Importar o modelo Informativo
import com.example.myapplication.model.Usuario
import com.example.myapplication.utils.SUPABASE_API_KEY
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query // Importar Query


interface SupabaseService {

    // --- Métodos de Usuário --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/usuario")
    suspend fun criarUsuario(@Body usuario: Usuario): List<Usuario>


    // --- Métodos de Informativo --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/informativo") // Endpoint para buscar informativos
    suspend fun getInformativos(
        @Query("select") select: String = "*", // Seleciona todas as colunas por padrão
        @Query("order") order: String = "criado_em.desc" // Ordena pelos mais recentes (opcional, ajuste se necessário)
    ): List<Informativo> // Retorna uma lista de objetos Informativo

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/informativo")
    suspend fun criarInformativo(@Body informativo: Informativo): List<Informativo>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/informativo")
    suspend fun atualizarInformativo(
        @Query("id") id: String,
        @Body dadosAtualizados: AtualizacaoInformativo
    )

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/informativo")
    suspend fun deletarInformativo(
        @Query("id") id: String
    )



}
