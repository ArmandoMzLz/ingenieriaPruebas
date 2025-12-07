package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "conversations",
    foreignKeys = [
        ForeignKey(
            entity = userEntity::class,
            parentColumns = ["userEmail"],
            childColumns = ["ownerEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = walkerEntity::class,
            parentColumns = ["walkerEmail"],
            childColumns = ["walkerEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class conversationEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerEmail: String,
    val walkerEmail: String,
    val date: Long
)