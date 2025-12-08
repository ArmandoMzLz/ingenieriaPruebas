package com.example.animalcrossing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.data.entity.petEntity
import com.example.animalcrossing.data.entity.walkerRequestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {

    private lateinit var petRecyclerView: RecyclerView
    private lateinit var petAdapter: PetAdapter
    private lateinit var emptyMessage: TextView
    private lateinit var acceptedCard: CardView
    private lateinit var acceptedPetName: TextView
    private lateinit var acceptedRouteName: TextView
    private lateinit var btnStartWalk: Button
    private lateinit var userEmail: String
    private lateinit var userName: String
    private lateinit var userRole: String

    companion object {
        private const val ARG_USERNAME = "UserName"
        private const val ARG_USEREMAIL = "UserEmail"
        private const val ARG_USERROLE = "UserRole"

        fun newInstance(userName: String, userEmail: String, userRole: String): Home {
            val fragment = Home()
            val args = Bundle()
            args.apply {
                putString(ARG_USERNAME, userName)
                putString(ARG_USEREMAIL, userEmail)
                putString(ARG_USERROLE, userRole)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        userName = arguments?.getString(ARG_USERNAME) ?: "Usuario"
        userEmail = arguments?.getString(ARG_USEREMAIL) ?: ""
        userRole = arguments?.getString(ARG_USERROLE) ?: "Owner"

        val userGreeting: TextView = view.findViewById(R.id.userGreeting)
        userGreeting.text = "Bienvenido, $userName"

        emptyMessage = view.findViewById(R.id.emptyMessage)
        petRecyclerView = view.findViewById(R.id.petRecycleViewer)
        petRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        petAdapter = PetAdapter(
            pets = emptyList(),
            onPetSelected = { pet -> openChooseRoute(pet, userEmail) }
        )
        petRecyclerView.adapter = petAdapter

        acceptedCard = view.findViewById(R.id.acceptedRequestCard)
        acceptedPetName = view.findViewById(R.id.acceptedRequestPetName)
        acceptedRouteName = view.findViewById(R.id.acceptedRequestRouteName)
        btnStartWalk = view.findViewById(R.id.startWalkButton)

        loadUserRole()

        return view
    }

    private fun loadUserRole() {
        if(userRole == "Owner") {
            showOwnerUI()
            loadPets()
        } else {
            showWalkerUI()
            loadAcceptedRequest()
        }
    }

    private fun loadPets() {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            db.petDao().getPetsByOwner(userEmail).collect { petList ->
                withContext(Dispatchers.Main) {
                    petAdapter.updateData(petList)
                    emptyMessage.visibility =
                        if (petList.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun showOwnerUI() {
        acceptedCard.visibility = View.GONE
        petRecyclerView.visibility = View.VISIBLE
        emptyMessage.visibility = View.VISIBLE
    }

    private fun openChooseRoute(pet: petEntity, userEmail: String) {
        val fragment = ChooseRoute.newInstance(pet.id, pet.name, userEmail)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_contanier, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showWalkerUI() {
        acceptedCard.visibility = View.VISIBLE
        petRecyclerView.visibility = View.GONE
        emptyMessage.visibility = View.GONE
    }

    private fun loadAcceptedRequest() {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            db.walkerRequestDao().getAcceptedRequestsByWalker(userEmail)
                .collect { acceptedList ->

                    withContext(Dispatchers.Main) {
                        if (acceptedList.isNotEmpty()) {
                            val req = acceptedList.first()

                            acceptedCard.visibility = View.VISIBLE
                            acceptedPetName.text = "Mascota: ${req.petName}"
                            acceptedRouteName.text = "Ruta: ${req.routeName}"

                            btnStartWalk.setOnClickListener {
                                startWalk(req)
                            }
                        }
                    }
                }
        }
    }

    private fun startWalk(request: walkerRequestEntity) {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            db.walkerRequestDao().updateStatus(request.id, "En curso")

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Paseo iniciado", Toast.LENGTH_SHORT).show()
                acceptedCard.visibility = View.GONE
            }
        }
    }
}