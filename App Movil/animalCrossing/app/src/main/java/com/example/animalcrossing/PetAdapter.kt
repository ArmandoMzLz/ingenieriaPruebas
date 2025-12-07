package com.example.animalcrossing

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animalcrossing.data.entity.petEntity

class PetAdapter (
    private var pets: List<petEntity>
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {
    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petImage: ImageView = itemView.findViewById(R.id.petImage)
        val petName: TextView = itemView.findViewById(R.id.petName)
        val petBreed: TextView = itemView.findViewById(R.id.petBreed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet_card, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]

        holder.petName.text = pet.name
        holder.petBreed.text = pet.breed

        val uri = Uri.parse(pet.photoUri)
        holder.petImage.setImageURI(uri)
    }

    override fun getItemCount(): Int = pets.size

    fun updateData(newPets: List<petEntity>) {
        pets = newPets
        notifyDataSetChanged()
    }
}