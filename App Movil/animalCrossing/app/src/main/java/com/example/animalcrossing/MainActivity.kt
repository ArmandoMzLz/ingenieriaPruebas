package com.example.animalcrossing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private lateinit var userEmailInput: EditText
    private lateinit var userPasswordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        val session = SessionManager(this)
        val savedEmail = session.getUser()

        if(savedEmail != null) {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
            finish()
        }

        userEmailInput = findViewById(R.id.userEmail)
        userPasswordInput = findViewById(R.id.userPassword)

        loginButton = findViewById(R.id.buttonLogin)
        registerButton = findViewById(R.id.buttonRegister)

        loginButton.setOnClickListener {
            val inputEmail = userEmailInput.text.toString()
            val inputPassword = userPasswordInput.text.toString()

            login(inputEmail, inputPassword)
        }

        registerButton.setOnClickListener {
            val intentRegister = Intent(this@MainActivity, Register::class.java)
            startActivity(intentRegister)
        }
    }

    private fun login(email: String, password: String) {
        val db = dataBaseProvider.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            val user = db.userDao().getUserById(email)

            withContext(Dispatchers.Main) {
                if(user == null || user.password != password) {
                    Toast.makeText(this@MainActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this@MainActivity, MainMenu::class.java).apply {
                        putExtra("UserName", user.name)
                        putExtra("UserEmail", user.userEmail)
                        putExtra("UserRole", user.role)
                    }
                    startActivity(intent)
                }
            }
        }
    }
}