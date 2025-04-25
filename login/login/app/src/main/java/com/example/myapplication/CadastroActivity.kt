package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro)
        FirebaseApp.initializeApp(this)

        val db = FirebaseFirestore.getInstance()

        val btn = findViewById<Button>(R.id.btnCadastrar)
        btn.setOnClickListener {

            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val senha = findViewById<EditText>(R.id.etSenha).text.toString()
            val confirmarSenha = findViewById<EditText>(R.id.etConfirmarSenha).text.toString()


            if (email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            } else if (senha != confirmarSenha) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("usuarios")
                    .whereEqualTo("email", email) // Filtra por e-mail
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) { // Se houver documentos com o mesmo e-mail
                            Toast.makeText(this, "Este e-mail já está registrado!", Toast.LENGTH_SHORT).show()
                        } else {
                            val user = hashMapOf(
                                "email" to email,
                                "senha" to senha
                            )

                            db.collection("usuarios")
                                .add(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Usuário salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao verificar o e-mail", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
