package com.example.animalcrossing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.R
import com.example.animalcrossing.data.PredefinedRoute

class RouteAdapter (
    private val routes: List<PredefinedRoute>,
    private val onClick: (PredefinedRoute) -> Unit
) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameRoute = itemView.findViewById<TextView>(R.id.routeName)
        val price = itemView.findViewById<TextView>(R.id.routePrice)
        val timeDistance = itemView.findViewById<TextView>(R.id.routeTimeDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_route_card, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val route = routes[position]

        holder.nameRoute.text = route.name
        holder.price.text = route.price
        holder.timeDistance.text = "${route.time} - ${route.distance}"

        holder.itemView.setOnClickListener {
            onClick(route)
        }
    }

    override fun getItemCount() = routes.size
}