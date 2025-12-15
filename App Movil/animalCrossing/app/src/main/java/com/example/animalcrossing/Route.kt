package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.adapters.RouteAdapter
import com.example.animalcrossing.data.PredefinedRoute
import com.example.animalcrossing.data.PredefinedRoutes

class Route : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.routesRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        recycler.adapter = RouteAdapter(PredefinedRoutes.allRoutes) { selectedRoute ->
            openMap(selectedRoute)
        }
    }

    private fun openMap(route: PredefinedRoute) {
        val fragment = RouteMapFragment()

        val bundle = Bundle()
        bundle.putSerializable("route", route)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_contanier, fragment)
            .addToBackStack(null)
            .commit()
    }
}