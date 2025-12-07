package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = userEntity::class,
            parentColumns = ["userEmail"],
            childColumns = ["userEmisor"]
        ),
        ForeignKey(
            entity = walkerEntity::class,
            parentColumns = ["walkerEmail"],
            childColumns = ["walkerReciever"]
        )
    ])
data class messageEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userEmisor: String,
    val walkerReciever: String,
    val message: String,
    val date: Long,
    val isRead: Boolean
)