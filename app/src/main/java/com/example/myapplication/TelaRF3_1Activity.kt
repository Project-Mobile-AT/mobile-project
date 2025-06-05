package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.fragments.BottomNavFragment
import com.example.myapplication.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class TelaRF3_1Activity : AppCompatActivity() {

    private lateinit var imageViewProfile: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonUploadImage: Button
    private lateinit var buttonLoadImage: Button
    private lateinit var progressBar: View

    private lateinit var textViewUserName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewCpf: TextView
    private lateinit var textViewDataNascimento: TextView

    private var userId: String? = null

    private val supabaseService = SupabaseClient.service

    private val PICK_IMAGE_REQUEST = 1

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf3_1)

        // Inicializa as views
        imageViewProfile = findViewById(R.id.imageViewProfile)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonUploadImage = findViewById(R.id.buttonUploadImage)
        buttonLoadImage = findViewById(R.id.buttonLoadImage)
        progressBar = findViewById(R.id.progressBar)

        textViewUserName = findViewById(R.id.textViewUserName)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewCpf = findViewById(R.id.textViewCpf)
        textViewDataNascimento = findViewById(R.id.textViewDataNascimento)

        // Obtém o userId do SharedPreferences
        userId = getLoggedInUserId()
        if (userId == null) {
            Log.w("TelaRF3_1Activity", "Nenhum ID de usuário encontrado no SharedPreferences.")
        } else {
            Log.d("TelaRF3_1Activity", "ID do usuário obtido do SharedPreferences: $userId")
        }

        // Configuração do Menu Popup
        val menuButton = findViewById<ImageView>(R.id.id_menu)
        menuButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_informativos -> {
                        Toast.makeText(this, "Abrindo Informativos", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_sair -> {
                        Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                        clearUserId()
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        // Listener para o botão de selecionar imagem
        buttonSelectImage.setOnClickListener {
            openImageChooser()
        }

        // Listener para o botão de salvar/upload da imagem
        buttonUploadImage.setOnClickListener {
            if (userId != null) {
                if (selectedImageUri != null) {
                    uploadProfileImage(selectedImageUri!!)
                } else {
                    Toast.makeText(this, "Por favor, selecione uma imagem primeiro.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "ID de usuário não encontrado. Faça login para salvar a imagem.", Toast.LENGTH_LONG).show()
            }
        }

        // Listener para o botão de carregar a imagem
        buttonLoadImage.setOnClickListener {
            if (userId != null) {
                // CORRIGIDO AQUI: Chamando loadProfileDataAndImage dentro de uma coroutine
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("LoadButton", "Botão Carregar Foto clicado. UserID: $userId")
                    loadProfileDataAndImage(userId!!)
                }
            } else {
                Toast.makeText(this, "ID de usuário não encontrado. Faça login para carregar a imagem.", Toast.LENGTH_LONG).show()
            }
        }

        // Carrega o fragmento de navegação inferior
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottom_nav, BottomNavFragment())
            .commit()

        // Carrega os dados e a imagem de perfil ao iniciar a Activity, se o userId estiver disponível
        if (userId != null) {
            Log.d("TelaRF3_1Activity", "Carregando dados e imagem ao iniciar. UserID: $userId")
            // CORRIGIDO AQUI: Chamando loadProfileDataAndImage dentro de uma coroutine
            CoroutineScope(Dispatchers.IO).launch {
                loadProfileDataAndImage(userId!!)
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            imageViewProfile.setImageURI(selectedImageUri)
        }
    }

    private fun getLoggedInUserId(): String? {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("logged_in_user_id", null)
    }

    private fun clearUserId() {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("logged_in_user_id")
            apply()
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun convertImageToBase64(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            val resizedBitmap = resizeBitmap(originalBitmap, 200)
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

            val bytes = outputStream.toByteArray()
            val base64String = Base64.encode(bytes)
            Log.d("ImageConverter", "Imagem convertida para Base64. Tamanho da string: ${base64String.length}")
            base64String
        } catch (e: Exception) {
            Log.e("ImageConverter", "Erro ao converter imagem para Base64: ${e.message}", e)
            Toast.makeText(this, "Falha na conversão da imagem.", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val ratio: Float

        if (width > height) {
            ratio = maxDimension.toFloat() / width
            return Bitmap.createScaledBitmap(bitmap, maxDimension, (height * ratio).toInt(), true)
        } else {
            ratio = maxDimension.toFloat() / height
            return Bitmap.createScaledBitmap(bitmap, (width * ratio).toInt(), maxDimension, true)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            Log.d("ImageConverter", "Tentando decodificar Base64 para Bitmap. Tamanho da string: ${base64String.length}")
            val decodedBytes = Base64.decode(base64String)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            if (bitmap == null) {
                Log.e("ImageConverter", "Bitmap retornado pela decodificação é NULO.")
            }
            bitmap
        } catch (e: Exception) {
            Log.e("ImageConverter", "Erro ao decodificar Base64 para Bitmap: ${e.message}", e)
            Toast.makeText(this, "Falha ao exibir imagem. Formato inválido.", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun uploadProfileImage(imageUri: Uri) {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val base64String = convertImageToBase64(imageUri)

            if (base64String != null) {
                val currentLoggedInUserId = userId!!

                try {
                    val existingUsers = supabaseService.getUsuarioById(id = "eq.$currentLoggedInUserId")

                    if (existingUsers.isNotEmpty()) {
                        val existingUser = existingUsers.first()
                        val updatedUser = existingUser.copy(img_perfil = base64String)

                        supabaseService.atualizarUsuario(
                            id = "eq.$currentLoggedInUserId",
                            usuario = updatedUser
                        )
                        Log.d("Upload", "Imagem de perfil salva com sucesso para ID: $currentLoggedInUserId")

                        Log.d("Upload", "Recarregando dados do perfil após upload.")
                        loadProfileDataAndImage(currentLoggedInUserId)

                    } else {
                        Log.e("SupabaseUpload", "Erro: Usuário com ID ${currentLoggedInUserId} não encontrado no DB para atualização. Não foi possível salvar a imagem.")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@TelaRF3_1Activity, "Erro: Perfil de usuário não encontrado para atualização.", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SupabaseUpload", "Erro ao salvar imagem de perfil para ${currentLoggedInUserId}: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TelaRF3_1Activity, "Erro ao salvar imagem: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
            }
        }
    }

    // Função modificada para carregar TODOS os dados do perfil
    private fun loadProfileImage() {
        progressBar.visibility = View.VISIBLE
        // Removemos o CoroutineScope.launch daqui, pois a função já é chamada por outra coroutine
        // ou já chamaremos loadProfileDataAndImage diretamente dentro de uma coroutine.
        // A lógica de CoroutineScope().launch já está no buttonLoadImage.setOnClickListener
        // e no if (userId != null) do onCreate.
        // Se esta função loadProfileImage() fosse chamada de outros locais síncronos,
        // ela precisaria de um CoroutineScope.launch {} ao redor da chamada a loadProfileDataAndImage.
        // Mas como ela é um "wrapper" para loadProfileDataAndImage, mantemos a simplicidade.
        CoroutineScope(Dispatchers.IO).launch { // Reintroduzido para garantir contexto para outras chamadas.
            val currentLoggedInUserId = userId!!
            Log.d("LoadImage", "Iniciando carregamento de imagem. UserID: $currentLoggedInUserId")
            loadProfileDataAndImage(currentLoggedInUserId)
        }
    }

    private suspend fun loadProfileDataAndImage(id: String) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("ImageLoad", "Buscando dados do usuário com ID: $id")
                val users = supabaseService.getUsuarioById(id = "eq.$id")

                withContext(Dispatchers.Main) {
                    if (users.isNotEmpty()) {
                        val userProfile = users.first()
                        Log.d("ImageLoad", "Usuário encontrado. Nome: ${userProfile.nome}. Campo img_perfil está nulo? ${userProfile.img_perfil == null}")

                        textViewUserName.text = userProfile.nome ?: "Nome não disponível"
                        textViewEmail.text = userProfile.email ?: "Email não disponível"
                        textViewCpf.text = userProfile.cpf ?: "CPF não disponível"
                        textViewDataNascimento.text = userProfile.data_nascimento ?: "Data de Nasc. não disponível"

                        userProfile.img_perfil?.let { base64 ->
                            Log.d("ImageLoad", "Campo img_perfil NÃO é nulo. Tentando decodificar imagem. Tamanho Base64: ${base64.length}")
                            val bitmap = decodeBase64ToBitmap(base64)
                            bitmap?.let {
                                imageViewProfile.setImageBitmap(it)
                                Log.d("ImageLoad", "Imagem definida na ImageView com sucesso.")
                            } ?: run {
                                Log.e("ImageLoad", "Não foi possível decodificar o Bitmap da imagem para o usuário: $id. Exibindo padrão.")
                            }
                        } ?: run {
                            Log.d("ImageLoad", "Campo img_perfil é NULO no DB para o usuário: $id. Exibindo padrão.")
                        }
                    } else {
                        Log.d("ImageLoad", "Usuário com ID: $id não encontrado no DB. Exibindo imagem padrão e dados vazios.")
                        textViewUserName.text = "Usuário Desconhecido"
                        textViewEmail.text = "N/A"
                        textViewCpf.text = "N/A"
                        textViewDataNascimento.text = "N/A"
                    }
                }
            } catch (e: Exception) {
                Log.e("SupabaseLoad", "Erro ao carregar dados do perfil para ${id}: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF3_1Activity, "Erro ao carregar dados do perfil: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}