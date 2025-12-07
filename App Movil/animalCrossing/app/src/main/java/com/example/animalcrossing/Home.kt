package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.database.dataBaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Home : Fragment() {
    private lateinit var petRecyclerView: RecyclerView
    private lateinit var adapter: PetAdapter

    companion object {
        private const val ARG_USERNAME = "UserName"
        private const val ARG_USEREMAIL = "UserEmail"

        fun newInstance(userName: String, userEmail: String): Home {
            val fragment = Home()
            val args = Bundle()

            args.apply {
                putString(ARG_USERNAME, userName)
                putString(ARG_USEREMAIL, userEmail)
            }

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val username = arguments?.getString(ARG_USERNAME) ?: "Usuario"
        val useremail = arguments?.getString(ARG_USEREMAIL) ?: "Correo"
        val userGreeting: TextView = view.findViewById(R.id.userGreeting)
        userGreeting.text = "Bienvenido, $username"

        adapter = PetAdapter(
            pets = emptyList(),
            onPetSelected = { pet ->
                openChooseRoute(pet, useremail)
            }
        )

        petRecyclerView = view.findViewById(R.id.petRecycleViewer)
        petRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        petRecyclerView.adapter = adapter

        loadPets(useremail)

        return view
    }

    private fun openChooseRoute(pet: com.example.animalcrossing.data.entity.petEntity, userEmail: String) {
        val fragment = ChooseRoute.newInstance(pet.id, pet.name, userEmail)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_contanier, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadPets(userEmail: String) {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            db.petDao().getPetsByOwner(userEmail).collect { petList ->
                launch(Dispatchers.Main) {
                    adapter.updateData(petList)
                }
            }
        }
    }
}