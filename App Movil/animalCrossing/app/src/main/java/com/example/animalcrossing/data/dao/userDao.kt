package com.example.animalcrossing.data.dao

import androidx.room.*
import com.example.animalcrossing.data.entity.userEntity

@Dao
interface userDao {
    @Insert
    suspend fun insertUser(user: userEntity)

    @Delete
    suspend fun deleteUser(user: userEntity)

    @Query("SELECT * FROM users WHERE userEmail = :email")
    fun getUserById(email: String): userEntity?

    @Query("UPDATE users SET password = :newPassword, name = :newName, address = :newAddress, telephoneNumber = :newTelephoneNumber WHERE userEmail = :email")
    fun updateUserById(email: String, newPassword: String, newName: String, newAddress: String, newTelephoneNumber: String): Int

    @Query("SELECT * FROM users where role = 'Walker'")
    suspend fun getWalkerUsers(): List<userEntity>
}