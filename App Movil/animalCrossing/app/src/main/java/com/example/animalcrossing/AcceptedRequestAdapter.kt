package com.example.animalcrossing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.entity.walkerRequestEntity

class AcceptedRequestAdapter(
    private var requests: List<walkerRequestEntity>,
    private val onSelect: (walkerRequestEntity) -> Unit
) : RecyclerView.Adapter<AcceptedRequestAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petName = view.findViewById<TextView>(R.id.petName)
        val btnStartWalk = view.findViewById<Button>(R.id.startWalkButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_accepted_requests, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requests[position]

        holder.petName.text = request.petName

        holder.btnStartWalk.setOnClickListener {
            onSelect(request)
        }
    }

    override fun getItemCount() = requests.size

    fun updateData(newData: List<walkerRequestEntity>) {
        requests = newData
        notifyDataSetChanged()
    }
}