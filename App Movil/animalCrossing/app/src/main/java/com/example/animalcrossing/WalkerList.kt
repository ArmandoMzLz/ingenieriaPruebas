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
import com.example.animalcrossing.data.entity.userEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalkerList : Fragment() {

    companion object {
        private const val ARG_ROUTE = "selectedRoute"
        private const val ARG_PET_ID = "petId"
        private const val ARG_PET_NAME = "petName"
        private const val ARG_USER = "userData"

        fun newInstance(
            route: PredefinedRoute,
            petId: Int,
            petName: String,
            user: userEntity
        ): WalkerList {
            val fragment = WalkerList()
            val args = Bundle()

            args.putSerializable(ARG_ROUTE, route)
            args.putInt(ARG_PET_ID, petId)
            args.putString(ARG_PET_NAME, petName)
            args.putSerializable(ARG_USER, user)

            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WalkerAdapter
    private lateinit var emptyMessage: TextView

    private lateinit var selectedRoute: PredefinedRoute
    private var petId: Int = -1
    private var petName: String = ""
    private lateinit var user: userEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_walker_list, container, false)

        recyclerView = view.findViewById(R.id.walkersRecycler)
        emptyMessage = view.findViewById(R.id.emptyWalkerMessage)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        selectedRoute = requireArguments().getSerializable(ARG_ROUTE) as PredefinedRoute
        petId = requireArguments().getInt(ARG_PET_ID)
        petName = requireArguments().getString(ARG_PET_NAME) ?: ""
        user = requireArguments().getSerializable(ARG_USER) as userEntity

        adapter = WalkerAdapter(emptyList()) { walker ->
            openWalkerProfile(walker)
        }

        recyclerView.adapter = adapter

        loadWalkers()

        return view
    }

    private fun loadWalkers() {
        val db = dataBaseProvider.getDatabase(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val walkers = db.userDao().getWalkerUsers()

            launch(Dispatchers.Main) {

                adapter.updateData(walkers)

                if (walkers.isEmpty()) {
                    emptyMessage.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openWalkerProfile(walker: userEntity) {
        val fragment = WalkerProfile.newInstance(
            walker = walker,
            route = selectedRoute,
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