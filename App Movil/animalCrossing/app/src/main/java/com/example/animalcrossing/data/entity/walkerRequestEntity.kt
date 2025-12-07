package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "walkerRequests")
data class walkerRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walkerEmail: String,
    val ownerEmail: String,
    val petId: Int,
    val petName: String,
    val routeName: String,
    val status: String,      // Pendiente, Aceptada, EnCurso, Completada
    val timestamp: Long
)
