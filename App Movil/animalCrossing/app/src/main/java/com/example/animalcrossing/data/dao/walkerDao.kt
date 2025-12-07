package com.example.animalcrossing.data.dao

import androidx.room.*
import com.example.animalcrossing.data.entity.walkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface walkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalker(walker: walkerEntity)

    @Query("SELECT * FROM walkers WHERE walkerEmail = :email")
    suspend fun getWalkerByEmail(email: String): walkerEntity?

    @Query("SELECT * FROM walkers")
    fun getAllWalkers(): List<walkerEntity>
}