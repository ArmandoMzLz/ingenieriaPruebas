package com.example.animalcrossing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class userEntity (
    @PrimaryKey(autoGenerate = false) val userEmail: String,
    val password: String,
    val name: String,
    val address: String,
    val telephoneNumber: String,
    val role: String
) : Serializable