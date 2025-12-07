package com.example.animalcrossing

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.data.entity.petEntity
import com.example.animalcrossing.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import androidx.core.net.toUri

class Pet : Fragment() {
    private lateinit var petName: EditText
    private lateinit var petBreed: EditText
    private lateinit var petAge: EditText
    private lateinit var petDescription: EditText

    private lateinit var petPhoto: ImageView

    private lateinit var petSelectPhoto: Button
    private lateinit var petRegister: Button

    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private var selectedPhotoUri: String = ""

    companion object {
        private const val ARG_USEREMAIL = "UserEmail"

        fun newInstance(userEmail: String) : Pet {
            val fragment = Pet()
            val args = Bundle()
            args.putString(ARG_USEREMAIL, userEmail)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            uri ->
            if(uri != null) {
                selectedPhotoUri = uri.toString()
                petPhoto.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pet, container, false)

        val userEmail = arguments?.getString(ARG_USEREMAIL) ?: throw IllegalStateException("Pet Fragment requiere un userEmail válido")

        petName = view.findViewById(R.id.editTextName)
        petBreed = view.findViewById(R.id.editTextBreed)
        petAge = view.findViewById(R.id.editTextAge)
        petDescription = view.findViewById(R.id.editTextDescription)

        petPhoto = view.findViewById(R.id.imageDogPlaceholder)

        petSelectPhoto = view.findViewById(R.id.buttonSelectPhoto)
        petRegister = view.findViewById(R.id.buttonRegisterPet)

        petSelectPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        petRegister.setOnClickListener { registerPet(userEmail) }

        return view
    }

    private fun registerPet(userEmail: String) {
        val name = petName.text.toString()
        val breed = petBreed.text.toString()
        val age = petAge.text.toString().toInt()
        val description = petDescription.text.toString()

        if(name.isBlank() || breed.isBlank() || age.toString().isBlank() || description.isBlank() || selectedPhotoUri.isBlank()) {
            Toast.makeText(requireContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val internalPhotoPath = saveImageToInternalStorage(selectedPhotoUri.toUri())

        val pet = petEntity(
            name = name,
            breed = breed,
            description = description,
            age = age,
            photoUri = internalPhotoPath,
            ownerEmail = userEmail
        )

        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            db.petDao().insertPet(pet)

            launch(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Mascota registrada correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = "pet_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return file.absolutePath
    }
}