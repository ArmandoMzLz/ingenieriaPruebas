package com.example.animalcrossing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WalkerAdapter (
    private var walkers: List<UserWithWalkerData>,
    private val onClick: (UserWithWalkerData) -> Unit
) : RecyclerView.Adapter<WalkerAdapter.walkerViewHolder>() {

    inner class walkerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.walkerName)
        val email: TextView = view.findViewById(R.id.walkerEmail)
        val rating: TextView = view.findViewById(R.id.walkerRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): walkerViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_walker_card, parent, false)
        return walkerViewHolder(v)
    }

    override fun onBindViewHolder(holder: walkerViewHolder, position: Int) {
        val w  = walkers[position]

        holder.name.text = w.user?.name
        holder.email.text = w.user?.userEmail
        holder.rating.text = "${w.walker?.ratingAverage ?: "Sin reseñas"}"
        holder.itemView.setOnClickListener { onClick(w) }
    }

    override fun getItemCount(): Int = walkers.size

    fun update(new: List<UserWithWalkerData>) {
        walkers = new
        notifyDataSetChanged()
    }
}