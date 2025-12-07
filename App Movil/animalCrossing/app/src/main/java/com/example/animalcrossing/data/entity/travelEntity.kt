package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "travels",
    foreignKeys = [
        ForeignKey(
            entity = walkerEntity::class,
            parentColumns = ["walkerEmail"],
            childColumns = ["walkerEmail"]
        ),
        ForeignKey(
            entity = petsTravel::class,
            parentColumns = ["id"],
            childColumns = ["petsTravelID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = routeEntity::class,
            parentColumns = ["id"],
            childColumns = ["routeID"]
        )
    ])
data class travelEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walkerEmail: String,
    val petsTravelID: Int,
    val routeID: Int,
    val startingTime: Long,
    val endingTime: Long,
    val evidencePhoto: String
)