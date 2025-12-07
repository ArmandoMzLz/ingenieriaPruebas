package com.example.animalcrossing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.entity.userEntity

class WalkerAdapter(
    private var walkerList: List<userEntity>,
    private val onClick: (userEntity) -> Unit
) : RecyclerView.Adapter<WalkerAdapter.WalkerViewHolder>() {

    inner class WalkerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val walkerName: TextView = view.findViewById(R.id.walkerName)
        val walkerEmail: TextView = view.findViewById(R.id.walkerEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_walker_card, parent, false)
        return WalkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalkerViewHolder, position: Int) {
        val walker = walkerList[position]

        holder.walkerName.text = walker.name
        holder.walkerEmail.text = walker.userEmail

        holder.itemView.setOnClickListener { onClick(walker) }
    }

    override fun getItemCount(): Int = walkerList.size

    fun updateData(newList: List<userEntity>) {
        walkerList = newList
        notifyDataSetChanged()
    }
}