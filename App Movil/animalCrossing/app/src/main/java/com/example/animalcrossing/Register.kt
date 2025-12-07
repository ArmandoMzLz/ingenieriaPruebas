package com.example.animalcrossing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.data.entity.userEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class Register : ComponentActivity() {
    private lateinit var registerButton: Button

    private lateinit var radioGroup: RadioGroup

    private lateinit var userEmailInput: EditText
    private lateinit var userPasswordInput: EditText
    private lateinit var userNameInput: EditText
    private lateinit var userPhoneInput: EditText
    private lateinit var userAddressInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register)

        userEmailInput = findViewById(R.id.userEmailRegister)
        userPasswordInput = findViewById(R.id.userPasswordRegister)
        userNameInput = findViewById(R.id.userNameRegister)
        userPhoneInput = findViewById(R.id.userPhoneRegister)
        userAddressInput = findViewById(R.id.userAddressRegister)

        radioGroup = findViewById(R.id.radioGroupRole)

        registerButton = findViewById(R.id.button)

        registerButton.setOnClickListener {
            val inputEmail = userEmailInput.text.toString()
            val inputPassword = userPasswordInput.text.toString()
            val inputName = userNameInput.text.toString()
            val inputAddress = userAddressInput.text.toString()
            val inputPhone = userPhoneInput.text.toString()

            val selectedRole = radioGroup.checkedRadioButtonId
            val role = when(selectedRole) {
                R.id.radioButtonOwner -> "Owner"
                R.id.radioButtonWalker -> "Walker"
                else -> ""
            }

            //Validacion
            if(inputEmail.isBlank() || inputPassword.isBlank() || inputName.isBlank() || inputAddress.isBlank() || inputPhone.isBlank() || role.isBlank()){
                Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            register(inputEmail, inputPassword, inputName, inputAddress, inputPhone, role)
        }
    }

    private fun register(email: String, password: String, name: String, address: String, phone: String, role: String) {
        val db = dataBaseProvider.getDatabase(this)
        val user = userEntity(email, password, name, address, phone, role)
        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().insertUser(user)
        }

        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@Register, MainActivity::class.java)
        startActivity(intent)
    }
}