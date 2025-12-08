package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "walkerRequests",
    foreignKeys = [
        ForeignKey(
            entity = userEntity::class,
            parentColumns = ["userEmail"],
            childColumns = ["ownerEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = userEntity::class,
            parentColumns = ["userEmail"],
            childColumns = ["walkerEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = petEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class walkerRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walkerEmail: String,
    val ownerEmail: String,
    val petId: Int,
    val petName: String,
    val routeName: String,
    val status: String,      // Pendiente, Aceptada, EnCurso, Completada
)
