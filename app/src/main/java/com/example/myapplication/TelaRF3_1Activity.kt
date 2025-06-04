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
import android.widget.TextView // Importar TextView
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

    // Adicionado: TextViews para os dados do usuário
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

        imageViewProfile = findViewById(R.id.imageViewProfile)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonUploadImage = findViewById(R.id.buttonUploadImage)
        buttonLoadImage = findViewById(R.id.buttonLoadImage)
        progressBar = findViewById(R.id.progressBar)

        // Inicializa os TextViews
        textViewUserName = findViewById(R.id.textViewUserName)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewCpf = findViewById(R.id.textViewCpf)
        textViewDataNascimento = findViewById(R.id.textViewDataNascimento)

        userId = getLoggedInUserId()
        if (userId == null) {
            Log.w("TelaRF3_1Activity", "Nenhum ID de usuário encontrado no SharedPreferences.")
        } else {
            Log.d("TelaRF3_1Activity", "ID do usuário obtido do SharedPreferences: $userId")
        }

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

        buttonSelectImage.setOnClickListener {
            openImageChooser()
        }

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

        buttonLoadImage.setOnClickListener {
            if (userId != null) {
                loadProfileImage()
            } else {
                Toast.makeText(this, "ID de usuário não encontrado. Faça login para carregar a imagem.", Toast.LENGTH_LONG).show()
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottom_nav, BottomNavFragment())
            .commit()

        if (userId != null) {
            loadProfileImage()
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
            Base64.encode(bytes)
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
            val decodedBytes = Base64.decode(base64String)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
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

                        // Após o upload, recarrega todos os dados, incluindo a imagem
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
        CoroutineScope(Dispatchers.IO).launch {
            val currentLoggedInUserId = userId!!
            loadProfileDataAndImage(currentLoggedInUserId)
        }
    }

    // NOVA FUNÇÃO para carregar todos os dados (nome, email, cpf, data_nascimento e imagem)
    private suspend fun loadProfileDataAndImage(id: String) {
        withContext(Dispatchers.IO) { // Garante que a chamada de rede está em um thread de I/O
            try {
                val users = supabaseService.getUsuarioById(id = "eq.$id")

                withContext(Dispatchers.Main) { // Volta para o thread principal para atualizar a UI
                    if (users.isNotEmpty()) {
                        val userProfile = users.first()

                        // Atualiza os TextViews com os dados do usuário
                        textViewUserName.text = userProfile.nome ?: "Nome não disponível"
                        textViewEmail.text = userProfile.email ?: "Email não disponível"
                        textViewCpf.text = userProfile.cpf ?: "CPF não disponível"
                        textViewDataNascimento.text = userProfile.data_nascimento ?: "Data de Nasc. não disponível"

                        // Carrega a imagem de perfil
                        userProfile.img_perfil?.let { base64 ->
                            val bitmap = decodeBase64ToBitmap(base64)
                            bitmap?.let {
                                imageViewProfile.setImageBitmap(it)
                            } ?: run {
                                Log.e("ImageLoad", "Não foi possível decodificar a imagem para o usuário: $id. Exibindo padrão.")
                                // Mantenha a imagem padrão do XML, não mude nada
                            }
                        } ?: run {
                            Log.d("ImageLoad", "Nenhuma imagem de perfil encontrada no DB para o usuário: $id. Exibindo padrão.")
                            // Mantenha a imagem padrão do XML, não mude nada
                        }
                    } else {
                        Log.d("ImageLoad", "Usuário não encontrado no DB: $id. Exibindo imagem padrão e dados vazios.")
                        // Limpa/Reseta os TextViews se o usuário não for encontrado
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