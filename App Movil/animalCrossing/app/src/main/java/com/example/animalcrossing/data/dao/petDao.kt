package com.example.animalcrossing.data.dao

import androidx.room.*
import com.example.animalcrossing.data.entity.petEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface petDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: petEntity)

    @Delete
    suspend fun deletePet(pet: petEntity)

    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<petEntity>>

    @Query("SELECT * FROM pets WHERE ownerEmail = :email")
    fun getPetsByOwner(email: String): Flow<List<petEntity>>
}