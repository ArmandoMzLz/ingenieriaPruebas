package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "petsTravel",
    foreignKeys = [
        ForeignKey(
            entity = petEntity::class,
            parentColumns = ["id"],
            childColumns = ["petID"]
        )
    ])
data class petsTravel (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val petID: Int
)