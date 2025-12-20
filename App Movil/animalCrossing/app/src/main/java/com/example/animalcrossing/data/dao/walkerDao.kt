package com.example.animalcrossing.data.dao

import androidx.room.*
import com.example.animalcrossing.WalkerWithRating
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

    @Query("SELECT u.name AS name, u.userEmail AS email, w.ratingAverage AS ratingAverage FROM users u LEFT JOIN walkers w ON u.userEmail = w.walkerEmail WHERE u.role = 'Walker'")
    fun getWalkersWithRating(): Flow<List<WalkerWithRating>>
}