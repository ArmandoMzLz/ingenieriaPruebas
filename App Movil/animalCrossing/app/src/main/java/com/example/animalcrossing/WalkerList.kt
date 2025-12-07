package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.database.dataBaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class WalkerList : Fragment() {

    private var pet: com.example.animalcrossing.data.entity.petEntity? = null
    private lateinit var route: PredefinedRoute
    private lateinit var adapter: WalkerAdapter
    private val db by lazy { dataBaseProvider.getDatabase(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pet = arguments?.getSerializable("pet") as? com.example.animalcrossing.data.entity.petEntity
        route = arguments?.getSerializable("route") as PredefinedRoute
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.walkersRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = WalkerAdapter(emptyList()) { selected ->
            val frag = RouteMapFragment()
            val b = Bundle()
            b.putSerializable("pet", pet)
            b.putSerializable("route", route)
            b.putSerializable("walkerUser", selected.user)
            b.putSerializable("walkerData", selected.walker)
            frag.arguments = b

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_contanier, frag)
                .addToBackStack(null)
                .commit()
        }

        recycler.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val walkers = db.walkerDao().getAllWalkers()
            val list = mutableListOf<UserWithWalkerData>()

            walkers.forEach { w ->
                val u = db.userDao().getUserById(w.walkerEmail)
                list.add(UserWithWalkerData(u, w))
            }

            launch(Dispatchers.Main) {
                adapter.update(list)
            }
        }
    }
}