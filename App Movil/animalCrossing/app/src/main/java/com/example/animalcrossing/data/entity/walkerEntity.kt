package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.io.Serializable

@Entity(tableName = "walkers",
    foreignKeys = [
        ForeignKey(
            entity = userEntity::class,
            parentColumns = ["userEmail"],
            childColumns = ["walkerEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class walkerEntity (
    @PrimaryKey(autoGenerate = false) val walkerEmail: String,
    val ratingSum: Float = 0f,
    val ratingCount: Int = 0,
    val ratingAverage: Float = 0f
) : Serializable