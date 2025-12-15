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
import android.widget.TextView
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.animalcrossing.adapters.WalkerRequestAdapter
import com.example.animalcrossing.data.entity.walkerRequestEntity

class Pet : Fragment() {
    private lateinit var petRegisterTitle: TextView
    private lateinit var petName: EditText
    private lateinit var petBreed: EditText
    private lateinit var petAge: EditText
    private lateinit var petDescription: EditText
    private lateinit var petPhoto: ImageView
    private lateinit var petSelectPhoto: Button
    private lateinit var petRegister: Button
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var requestTitle: TextView
    private lateinit var requestEmpty: TextView
    private lateinit var requestRecycler: RecyclerView
    private lateinit var requestAdapter: WalkerRequestAdapter
    private var selectedPhotoUri: String = ""
    private var userRole: String = "Owner"
    private lateinit var userEmail: String

    companion object {
        private const val ARG_USEREMAIL = "UserEmail"
        private const val ARG_USERROLE = "UserRole"

        fun newInstance(userEmail: String, userRole: String) : Pet {
            val fragment = Pet()
            val args = Bundle()
            args.apply {
                putString(ARG_USEREMAIL, userEmail)
                putString(ARG_USERROLE, userRole)
            }
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

        userEmail = arguments?.getString(ARG_USEREMAIL) ?: throw IllegalStateException("Pet Fragment requiere un userEmail válido")
        userRole = arguments?.getString(ARG_USERROLE) ?: "Owner"

        petRegisterTitle = view.findViewById(R.id.textView6)
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

        requestTitle = view.findViewById(R.id.walkerRequestsTitle)
        requestEmpty = view.findViewById(R.id.emptyRequests)
        requestRecycler = view.findViewById(R.id.walkerRequestsRecycler)

        requestRecycler.layoutManager = LinearLayoutManager(requireContext())
        requestAdapter = WalkerRequestAdapter(
            emptyList(),
            object : WalkerRequestAdapter.OnRequestActionListener {
                override fun onAccept(request: walkerRequestEntity) {
                    updateRequestStatus(request, "Aceptada")
                }

                override fun onReject(request: walkerRequestEntity) {
                    updateRequestStatus(request, "Rechazada")
                }
            }
        )

        requestRecycler.adapter = requestAdapter

        loadUserRole()

        return view
    }

    private fun loadUserRole() {
        if(userRole == "Owner") {
            showOwnerUI()
        } else {
            showWalkerUI()
        }
    }

    private fun showOwnerUI() {
        petRegisterTitle.visibility = View.VISIBLE
        petName.visibility = View.VISIBLE
        petBreed.visibility = View.VISIBLE
        petAge.visibility = View.VISIBLE
        petDescription.visibility = View.VISIBLE
        petPhoto.visibility = View.VISIBLE
        petSelectPhoto.visibility = View.VISIBLE
        petRegister.visibility = View.VISIBLE

        requestTitle.visibility = View.GONE
        requestRecycler.visibility = View.GONE
    }

    private fun showWalkerUI() {
        petRegisterTitle.visibility = View.GONE
        petName.visibility = View.GONE
        petBreed.visibility = View.GONE
        petAge.visibility = View.GONE
        petDescription.visibility = View.GONE
        petPhoto.visibility = View.GONE
        petSelectPhoto.visibility = View.GONE
        petRegister.visibility = View.GONE

        requestTitle.visibility = View.VISIBLE
        requestRecycler.visibility = View.VISIBLE

        loadRequests()
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

    private fun loadRequests() {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val requests = db.walkerRequestDao().getRequestsForWalker(userEmail)
            launch(Dispatchers.Main) {
                if(requests.isNotEmpty()) {
                    requestEmpty.visibility = View.GONE
                    requestRecycler.visibility = View.VISIBLE
                    requestAdapter.updateData(requests)
                } else {
                    requestEmpty.visibility = View.VISIBLE
                    requestRecycler.visibility = View.GONE
                }
            }
        }
    }

    private fun updateRequestStatus(request: walkerRequestEntity, newStatus: String) {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            db.walkerRequestDao().updateStatus(request.id, newStatus)

            val updated = db.walkerRequestDao().getRequestsForWalker(userEmail)

            launch(Dispatchers.Main) {
                if (updated.isNotEmpty()) {
                    requestTitle.visibility = View.VISIBLE
                    requestRecycler.visibility = View.VISIBLE
                    requestAdapter.updateData(updated)
                } else {
                    requestTitle.visibility = View.GONE
                    requestRecycler.visibility = View.GONE
                    requestEmpty.visibility = View.VISIBLE
                }
            }
        }
    }
}