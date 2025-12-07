package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.data.entity.walkerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Rating : Fragment() {

    private var walkerEmail: String = ""
    private val db by lazy { dataBaseProvider.getDatabase(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walkerEmail = requireArguments()?.getString("walkerEmail") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating, c, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val value = ratingBar.rating

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

                walkerEntity(
                    walkerEmail = walkerEmail,
                    ratingSum = newSum,
                    ratingCount = newCount,
                    ratingAverage = newAvg
                )
            }

            dao.insertWalker(newWalker)

            launch(Dispatchers.Main) {
                Toast.makeText(requireContext(), "¡Gracias por tu calificación!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }
}