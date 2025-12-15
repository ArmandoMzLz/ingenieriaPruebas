package com.example.animalcrossing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.R
import com.example.animalcrossing.WalkerWithRating
import com.example.animalcrossing.data.entity.userEntity
import org.w3c.dom.Text

class WalkerAdapter(
    private var walkerList: List<WalkerWithRating>,
    private val onClick: (WalkerWithRating) -> Unit
) : RecyclerView.Adapter<WalkerAdapter.WalkerViewHolder>() {

    inner class WalkerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val walkerName: TextView = view.findViewById(R.id.walkerName)
        val walkerEmail: TextView = view.findViewById(R.id.walkerEmail)
        val walkerRating: TextView = view.findViewById(R.id.walkerRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_walker_card, parent, false)
        return WalkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalkerViewHolder, position: Int) {
        val walker = walkerList[position]

        holder.walkerName.text = walker.name
        holder.walkerEmail.text = walker.email

        holder.walkerRating.text =
            if (walker.ratingAverage != null)
                "⭐ ${String.format("%.1f", walker.ratingAverage)}"
            else
                "⭐ Sin calificaciones"

        holder.itemView.setOnClickListener { onClick(walker) }
    }

    override fun getItemCount(): Int = walkerList.size

    fun updateData(newList: List<WalkerWithRating>) {
        walkerList = newList
        notifyDataSetChanged()
    }
}
