package com.example.animalcrossing

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Account : Fragment() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editName: EditText
    private lateinit var editAddress: EditText
    private lateinit var editTelephone: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userEmail = requireActivity()
            .intent
            .getStringExtra("UserEmail")
            ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        editEmail = view.findViewById(R.id.userEmailReadOnly)
        editPassword = view.findViewById(R.id.editTextUserPassword)
        editName = view.findViewById(R.id.editTextUserName)
        editAddress = view.findViewById(R.id.editTextUserAddress)
        editTelephone = view.findViewById(R.id.editTextUserTelephone)

        saveButton = view.findViewById(R.id.buttonSaveChanges)
        logoutButton = view.findViewById(R.id.buttonLogout)

        editEmail.setText(userEmail)
        editEmail.isEnabled = false

        getUserData()

        saveButton.setOnClickListener {
            updateUserData()
        }

        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun getUserData() {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val user = db.userDao().getUserById(userEmail)

            if (user != null) {
                launch(Dispatchers.Main) {
                    editName.setText(user.name)
                    editPassword.setText(user.password)
                    editAddress.setText(user.address)
                    editTelephone.setText(user.telephoneNumber)
                }
            }
        }
    }

    private fun updateUserData() {
        val newPassword = editPassword.text.toString()
        val newName = editName.text.toString()
        val newAddress = editAddress.text.toString()
        val newTelephoneNumber = editTelephone.text.toString()

        val db = dataBaseProvider.getDatabase(requireContext())

        if(newPassword.isBlank() || newName.isBlank() || newAddress.isBlank() || newTelephoneNumber.isBlank()) {
            Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val rows = db.userDao().updateUserById(userEmail, newPassword, newName, newAddress, newTelephoneNumber)

            CoroutineScope(Dispatchers.Main).launch {
                if(rows > 0) {
                    Toast.makeText(requireContext(), "Datos actualizados", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun logout() {
        val session = SessionManager(requireContext())
        session.clearSession()

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}