package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.io.Serializable

@Entity(tableName = "pets",
    foreignKeys = [
        ForeignKey(
            entity = userEntity::class,
            parentColumns = ["userEmail"],
            childColumns = ["ownerEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class petEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val breed: String,
    val description: String,
    val photoUri: String,
    val age: Int,
    val ownerEmail: String
) : Serializable