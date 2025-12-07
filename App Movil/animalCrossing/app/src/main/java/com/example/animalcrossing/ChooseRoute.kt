package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.database.dataBaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChooseRoute : Fragment() {
    companion object {
        private const val ARG_PET_ID = "petId"
        private const val ARG_PET_NAME = "petName"
        private const val ARG_USER_EMAIL = "userEmail"

        fun newInstance(petId: Int, petName: String, userEmail: String): ChooseRoute {
            val fragment = ChooseRoute()
            val args = Bundle()
            args.putInt(ARG_PET_ID, petId)
            args.putString(ARG_PET_NAME, petName)
            args.putString(ARG_USER_EMAIL, userEmail)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var routeRecycler: RecyclerView
    private lateinit var adapter: RouteAdapter

    private var petId: Int = -1
    private var petName: String = ""
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_route, container, false)

        petId = arguments?.getInt(ARG_PET_ID) ?: -1
        petName = arguments?.getString(ARG_PET_NAME) ?: ""
        userEmail = arguments?.getString(ARG_USER_EMAIL) ?: ""

        routeRecycler = view.findViewById(R.id.routeRecycler)
        routeRecycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = RouteAdapter(PredefinedRoutes.allRoutes) { selectedRoute ->
            navigateToWalkerList(selectedRoute)
        }

        routeRecycler.adapter = adapter

        return view
    }

    private fun navigateToWalkerList(route: PredefinedRoute) {

        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val user = db.userDao().getUserById(userEmail)

            launch(Dispatchers.Main) {
                if (user == null) {
                    Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val fragment = WalkerList.newInstance(
                    route = route,
                    petId = petId,
                    petName = petName,
                    user = user
                )

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_contanier, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}