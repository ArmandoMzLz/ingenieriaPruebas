package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "routes")
data class routeEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val latitude: Double,
    val longitude: Double,
    val distance: Float,
    val duration: Float
)