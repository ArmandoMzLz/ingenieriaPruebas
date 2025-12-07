package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class Route : Fragment() {
    private var pet: com.example.animalcrossing.data.entity.petEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pet = arguments?.getSerializable("pet") as? com.example.animalcrossing.data.entity.petEntity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_route_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.routesRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = RouteAdapter(PredefinedRoutes.allRoutes) { selectedRoute ->
            val frag = WalkerList()
            val b = Bundle()
            b.putSerializable("pet", pet as Serializable?)
            b.putSerializable("route", selectedRoute)
            frag.arguments = b

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_contanier, frag)
                .addToBackStack(null)
                .commit()
        }
    }
}