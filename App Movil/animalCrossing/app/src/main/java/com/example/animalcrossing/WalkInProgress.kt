package com.example.animalcrossing

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.animalcrossing.data.database.dataBaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WalkInProgress : Fragment() {
    private lateinit var textPet: TextView
    private lateinit var textRoute: TextView
    private lateinit var textTime: TextView
    private lateinit var imageEvidence: ImageView
    private lateinit var btnUploadPhoto: Button
    private lateinit var btnFinish: Button

    private lateinit var userEmail: String

    private var selectedImageUri: Uri? = null

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                imageEvidence.setImageURI(it)
            }
        }


    companion object {
        private const val ARG_USER_EMAIL = "UserEmail"

        fun newInstance(userEmail: String): WalkInProgress {
            val fragment = WalkInProgress()
            val args = Bundle()
            args.putString(ARG_USER_EMAIL, userEmail)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_walk_in_progress, container, false)

        userEmail = requireArguments().getString(ARG_USER_EMAIL) ?: error("UserEmail requerido")

        textPet = view.findViewById(R.id.walkPetName)
        textRoute = view.findViewById(R.id.walkRouteName)
        textTime = view.findViewById(R.id.walkTime)
        imageEvidence = view.findViewById(R.id.evidenceImage)
        btnUploadPhoto = view.findViewById(R.id.uploadPhotoButton)

        btnUploadPhoto.setOnClickListener {
            imagePicker.launch("image/*")
        }


        btnFinish = view.findViewById(R.id.finishWalkButton)

        observeWalk()

        return view
    }

    private fun observeWalk() {
        val db = dataBaseProvider.getDatabase(requireContext())

        lifecycleScope.launch {
            db.walkerRequestDao()
                .observeActiveWalkForWalker(userEmail)
                .collect { walk ->

                    if (walk == null) return@collect

                    textPet.text = "Mascota: ${walk.petName}"
                    textRoute.text = "Ruta: ${walk.routeName}"

                    if (walk.startTime != null) {
                        startTimer(walk.startTime)
                    }

                    btnFinish.setOnClickListener {
                        if (selectedImageUri == null) {
                            Toast.makeText(requireContext(), "Debes subir una foto como evidencia", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        finishWalk(walk.id)
                    }
                }
        }
    }

    private fun startTimer(startTime: Long) {
        lifecycleScope.launch {
            while (true) {
                val elapsed = System.currentTimeMillis() - startTime
                textTime.text = formatTime(elapsed)
                delay(1000)
            }
        }
    }

    private fun formatTime(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / 1000 / 60) % 60
        val hours = ms / 1000 / 60 / 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    private fun finishWalk(id: Int) {
        val db = dataBaseProvider.getDatabase(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            db.walkerRequestDao().updateStatus(id, "Finalizado")
        }

        parentFragmentManager.popBackStack()
    }
}