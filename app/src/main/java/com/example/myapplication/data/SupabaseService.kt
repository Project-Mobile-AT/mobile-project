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

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )

    @GET("rest/v1/usuario")
    suspend fun getUsuarioByEmailAndPassword(
        @Query("email") email: String,
        @Query("senha") senha: String
    ): List<Usuario>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/usuario")
    suspend fun getAlunos(
        @Query("is_admin") isAdmin: String = "eq.false"
    ): List<Usuario>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/usuario")
    suspend fun atualizarUsuario(
        @Query("id") id: String,
        @Body usuario: Usuario
    ): List<Usuario>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/usuario")
    suspend fun deletarUsuario(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

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

    // --- Métodos de Aula --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/aula")
    suspend fun getAulas(): List<com.example.myapplication.model.Aula>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/aula")
    suspend fun criarAula(@Body aula: com.example.myapplication.model.AulaPost): List<com.example.myapplication.model.Aula>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/aula")
    suspend fun atualizarAula(
        @Query("id") id: String,
        @Body aula: com.example.myapplication.model.AulaPatch
    ): List<com.example.myapplication.model.Aula>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/aula")
    suspend fun deletarAula(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/horario_atendimento")
    suspend fun getHorariosAtendimento(): List<com.example.myapplication.model.HorarioAtendimento>

}
