package com.example.myapplication
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TelaEntradaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_entrada)

        // Obter instância do Firestore
        val db = Firebase.firestore

        // Criar um documento de teste
        val dadosTeste = hashMapOf(
            "mensagem" to "Conexão com Firebase bem-sucedida!",
            "timestamp" to System.currentTimeMillis()
        )
        db.collection("aluno").add(mapOf(
            "nome" to "Narakao",
            "idade" to 35,
            "steamid" to "Narakao"
        ))
        db.collection("testeFirebase")
            .add(dadosTeste)
            .addOnSuccessListener { documentReference ->
                Log.d("FIREBASE_TESTE", "Documento adicionado com ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FIREBASE_TESTE", "Erro ao adicionar documento", e)
            }
    }
}