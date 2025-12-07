package com.example.animalcrossing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.animalcrossing.data.entity.walkerRequestEntity

@Dao
interface walkerRequestDao {

    @Insert
    suspend fun insertRequest(request: walkerRequestEntity)

    @Query("SELECT * FROM walkerRequests WHERE ownerEmail = :email")
    fun getRequestsByOwner(email: String): List<walkerRequestEntity>
}
