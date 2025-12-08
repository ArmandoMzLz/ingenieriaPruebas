package com.example.animalcrossing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.entity.walkerRequestEntity

class WalkerRequestAdapter(
    private var requests: List<walkerRequestEntity>,
    private val listener: OnRequestActionListener
) : RecyclerView.Adapter<WalkerRequestAdapter.RequestViewHolder>() {

    interface OnRequestActionListener {
        fun onAccept(request: walkerRequestEntity)
        fun onReject(request: walkerRequestEntity)
    }

    fun updateData(newList: List<walkerRequestEntity>) {
        requests = newList
        notifyDataSetChanged()
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOwner: TextView = itemView.findViewById(R.id.textOwner)
        val textPet: TextView = itemView.findViewById(R.id.textPet)
        val textRoute: TextView = itemView.findViewById(R.id.textRoute)
        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
        val buttonAccept: Button = itemView.findViewById(R.id.buttonAccept)
        val buttonReject: Button = itemView.findViewById(R.id.buttonReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_walk_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]

        holder.textOwner.text = "Dueño: ${request.ownerEmail}"
        holder.textPet.text = "Mascota: ${request.petName}"
        holder.textRoute.text = "Ruta: ${request.routeName}"
        holder.textStatus.text = "Estado: ${request.status}"

        val isPending = request.status == "Pendiente"
        holder.buttonAccept.isEnabled = isPending
        holder.buttonReject.isEnabled = isPending

        holder.buttonAccept.setOnClickListener {
            listener.onAccept(request)
        }

        holder.buttonReject.setOnClickListener {
            listener.onReject(request)
        }
    }

    override fun getItemCount(): Int = requests.size
}