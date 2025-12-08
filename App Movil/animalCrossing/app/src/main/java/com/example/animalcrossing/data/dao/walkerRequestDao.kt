package com.example.animalcrossing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.animalcrossing.data.entity.walkerRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface walkerRequestDao {

    @Insert
    suspend fun insertRequest(request: walkerRequestEntity)

    @Query("SELECT * FROM walkerRequests WHERE ownerEmail = :email")
    fun getRequestsByOwner(email: String): List<walkerRequestEntity>

    @Query("SELECT * FROM walkerRequests WHERE walkerEmail = :walkerEmail")
    suspend fun getRequestsForWalker(walkerEmail: String): List<walkerRequestEntity>

    @Query("UPDATE walkerRequests SET status = :newStatus WHERE id = :id")
    suspend fun updateStatus(id: Int, newStatus: String)

    @Query("SELECT * FROM walkerRequests WHERE walkerEmail = :walkerEmail AND status = 'Pendiente'")
    fun getPendingRequestsByWalker(walkerEmail: String): Flow<List<walkerRequestEntity>>

    @Query("SELECT * FROM walkerRequests WHERE walkerEmail = :walkerEmail AND status = 'Aceptada'")
    fun getAcceptedRequestsByWalker(walkerEmail: String): Flow<List<walkerRequestEntity>>
}
