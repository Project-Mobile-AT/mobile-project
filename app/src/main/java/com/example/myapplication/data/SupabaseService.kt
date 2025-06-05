package com.example.myapplication.data

import com.example.myapplication.model.AtualizacaoInformativo
import com.example.myapplication.model.Equipamento
import com.example.myapplication.model.Informativo
import com.example.myapplication.model.Presenca
import com.example.myapplication.model.RelacaoAdminAluno
import com.example.myapplication.model.Streak
import com.example.myapplication.model.Treino
import com.example.myapplication.model.Usuario
import com.example.myapplication.utils.SUPABASE_API_KEY
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

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

    //Método para buscar usuário pelo ID
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/usuario")
    suspend fun getUsuarioById(
        @Query("id") id: String
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
    @GET("rest/v1/informativo")
    suspend fun getInformativos(
        @Query("select") select: String = "*",
        @Query("order") order: String = "criado_em.desc"
    ): List<Informativo>

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

    // --- NOVOS Métodos para Streak, Treino e Presenca --- //

    // NOVO: Metodo para buscar a streak de um usuário
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/streak")
    suspend fun getStreakByUserId(
        @Query("usuario_id") userId: String // Busca por usuario_id
    ): List<Streak> // Retorna uma lista, deve ter no máximo 1 streak por usuário

//     NOVO: Metodo para buscar o treino de um usuário (considerando que há 1 treino "ativo" ou o mais recente)
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/treino")
    suspend fun getTreinoByUserId(
        @Query("usuario_id") userId: String,
        @Query("order") order: String = "criado_em.desc", // Pega o mais recente
        @Query("limit") limit: Int = 1 // Pega apenas um
    ): List<Treino>

    // NOVO: Metodo para registrar uma presença (check-in)
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/presenca")
    suspend fun createPresenca(@Body presenca: Presenca): List<Presenca>

    // --- Métodos de Equipamento --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/equipamento")
    suspend fun getEquipamentos(): List<Equipamento>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/equipamento")
    suspend fun criarEquipamento(@Body equipamento: Equipamento): List<Equipamento>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/equipamento")
    suspend fun atualizarEquipamento(
        @Query("id") id: String,
        @Body equipamento: Equipamento
    ): List<Equipamento>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/equipamento")
    suspend fun deletarEquipamento(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

    // --- Métodos de Exercício --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/exercicio")
    suspend fun getExercicios(
        @Query("select") select: String = "*"
    ): List<com.example.myapplication.model.Exercicio>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/exercicio")
    suspend fun criarExercicio(@Body exercicio: com.example.myapplication.model.Exercicio): List<com.example.myapplication.model.Exercicio>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/exercicio")
    suspend fun atualizarExercicio(
        @Query("id") id: String,
        @Body exercicio: com.example.myapplication.model.Exercicio
    ): List<com.example.myapplication.model.Exercicio>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/exercicio")
    suspend fun deletarExercicio(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

    // --- Métodos de Plano Alimentar --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/plano_alimentar")
    suspend fun getPlanoAlimentarByUsuarioId(
        @Query("usuario_id") usuarioId: String
    ): List<com.example.myapplication.model.PlanoAlimentar>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/plano_alimentar")
    suspend fun criarPlanoAlimentar(@Body plano: com.example.myapplication.model.PlanoAlimentar): List<com.example.myapplication.model.PlanoAlimentar>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/plano_alimentar")
    suspend fun atualizarPlanoAlimentar(
        @Query("id") id: String,
        @Body plano: com.example.myapplication.model.PlanoAlimentar
    ): List<com.example.myapplication.model.PlanoAlimentar>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/plano_alimentar")
    suspend fun deletarPlanoAlimentar(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

    // --- Métodos de Medidas do Aluno --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/medidas_aluno")
    suspend fun getMedidasByUsuarioId(
        @Query("usuario_id") usuarioId: String,
        @Query("order") order: String = "data_registro.desc",
        @Query("limit") limit: Int = 1
    ): List<com.example.myapplication.model.MedidasAluno>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/medidas_aluno")
    suspend fun criarMedidasAluno(@Body medidas: com.example.myapplication.model.MedidasAluno): List<com.example.myapplication.model.MedidasAluno>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/medidas_aluno")
    suspend fun atualizarMedidasAluno(
        @Query("id") id: String,
        @Body medidas: com.example.myapplication.model.MedidasAluno
    ): List<com.example.myapplication.model.MedidasAluno>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/medidas_aluno")
    suspend fun deletarMedidasAluno(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/horario_atendimento")
    suspend fun criarHorarioAtendimento(@Body horario: com.example.myapplication.model.HorarioAtendimento): List<com.example.myapplication.model.HorarioAtendimento>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/horario_atendimento")
    suspend fun atualizarHorarioAtendimento(
        @Query("id") id: String,
        @Body horario: com.example.myapplication.model.HorarioAtendimento
    ): List<com.example.myapplication.model.HorarioAtendimento>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/horario_atendimento")
    suspend fun deletarHorarioAtendimento(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

    // --- Métodos de Horário Nutricionista --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/horario_nutricionista")
    suspend fun getHorariosNutricionista(): List<com.example.myapplication.model.HorarioNutricionista>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/horario_nutricionista")
    suspend fun criarHorarioNutricionista(@Body horario: com.example.myapplication.model.HorarioNutricionista): List<com.example.myapplication.model.HorarioNutricionista>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/horario_nutricionista")
    suspend fun atualizarHorarioNutricionista(
        @Query("id") id: String,
        @Body horario: com.example.myapplication.model.HorarioNutricionista
    ): List<com.example.myapplication.model.HorarioNutricionista>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @DELETE("rest/v1/horario_nutricionista")
    suspend fun deletarHorarioNutricionista(
        @Query("id") id: String
    ): retrofit2.Response<Unit>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @POST("rest/v1/agendamento")
    suspend fun criarAgendamento(@Body agendamento: com.example.myapplication.model.Agendamento): List<com.example.myapplication.model.Agendamento>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/usuario") // Busca na tabela usuario
    suspend fun getAdmins(
        @Query("is_admin") isAdmin: String = "eq.true" // Filtra por is_admin = true
    ): List<Usuario>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/horario_atendimento") // <-- VERIFIQUE ESTE NOME DE TABELA/ENDPOINT!
    suspend fun getHorarios(): List<com.example.myapplication.model.HorarioAtendimento> // <-- VERIFIQUE ESTE NOME DE MODELO!

    // --- Métodos de RelacaoAdminAluno --- //
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json"
    )
    @GET("rest/v1/relacao_admin_aluno")
    suspend fun getAlunosByAdminId(
        @Query("admin_id") adminId: String,
        @Query("select") select: String = "aluno:aluno_id(nome,id)"
    ): List<RelacaoAdminAluno>

    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        ("Prefer: return=representation")
    )
    @POST("rest/v1/relacao_admin_aluno")
    suspend fun createRelacaoAdminAluno(
        @Body relacao: RelacaoAdminAluno
    ): List<RelacaoAdminAluno>

    // 1. Buscar Horário por ID
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY"
    )
    @GET("rest/v1/horario_atendimento")
    suspend fun getHorarioById(
        @Query("id") id: String, // Filtra por ID
        @Query("select") select: String = "*", // Garante que todos os campos sejam retornados
        @Query("limit") limit: Int = 1 // Espera apenas um resultado
    ): List<com.example.myapplication.model.HorarioAtendimento> // Retorna lista, mas deve conter 0 ou 1 item

    // Método para Atualizar Horário (PATCH)
    @Headers(
        "apikey: $SUPABASE_API_KEY",
        "Authorization: Bearer $SUPABASE_API_KEY",
        "Content-Type: application/json",
        "Prefer: return=representation"
    )
    @PATCH("rest/v1/horario_atendimento")
    suspend fun atualizarHorarioAtendimentoo(
        @Query("id") id: String, // Filtro pelo ID (ex: "eq.uuid-do-horario")
        @Body horarioUpdate: com.example.myapplication.model.HorarioAtendimento // <-- Verifique esta linha!
    ): List<com.example.myapplication.model.HorarioAtendimento>
}