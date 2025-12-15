package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.data.entity.walkerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Rating : Fragment() {

    private lateinit var walkerEmail: String
    private lateinit var ownerEmail: String
    private val db by lazy { dataBaseProvider.getDatabase(requireContext()) }

    companion object {
        private const val ARG_WALKER_EMAIL = "walkerEmail"
        private const val ARG_OWNER_EMAIL = "ownerEmail"

        fun newInstance(walkerEmail: String, ownerEmail: String): Rating {
            return Rating().apply {
                arguments = Bundle().apply {
                    putString(ARG_WALKER_EMAIL, walkerEmail)
                    putString(ARG_OWNER_EMAIL, ownerEmail)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        walkerEmail = requireArguments().getString(ARG_WALKER_EMAIL)
            ?: error("walkerEmail requerido")

        ownerEmail = requireArguments().getString(ARG_OWNER_EMAIL)
            ?: error("ownerEmail requerido")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val btnSubmit = view.findViewById<Button>(R.id.buttonSubmitRating)

        btnSubmit.setOnClickListener {

            val value = ratingBar.rating
            if (value == 0f) {
                Toast.makeText(requireContext(), "Selecciona una calificación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val dao = db.walkerDao()
                val existing = dao.getWalkerByEmail(walkerEmail)

                val newWalker = if (existing == null) {
                    walkerEntity(
                        walkerEmail = walkerEmail,
                        ratingSum = value,
                        ratingCount = 1,
                        ratingAverage = value
                    )
                } else {
                    val newSum = existing.ratingSum + value
                    val newCount = existing.ratingCount + 1
                    val newAvg = newSum / newCount

                    existing.copy(
                        ratingSum = newSum,
                        ratingCount = newCount,
                        ratingAverage = newAvg
                    )
                }

                dao.insertWalker(newWalker)

                db.walkerRequestDao()
                    .markWalkAsRated(ownerEmail)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "¡Gracias por tu calificación!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
}