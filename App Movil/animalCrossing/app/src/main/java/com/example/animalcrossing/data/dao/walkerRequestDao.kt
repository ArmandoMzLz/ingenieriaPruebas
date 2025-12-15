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

    @Query(" SELECT * FROM walkerRequests WHERE ownerEmail = :ownerEmail AND status = 'En curso' LIMIT 1")
    fun observeActiveWalkForOwner(ownerEmail: String): Flow<walkerRequestEntity?>

    @Query(" SELECT * FROM walkerRequests WHERE walkerEmail = :walkerEmail AND status = 'En curso' LIMIT 1")
    fun observeActiveWalkForWalker(walkerEmail: String): Flow<walkerRequestEntity?>

    @Query(" UPDATE walkerRequests SET status = :status, startTime = :startTime WHERE id = :id")
    suspend fun startWalk(id: Int, status: String, startTime: Long)

    @Query(" SELECT * FROM walkerRequests WHERE ownerEmail = :ownerEmail AND status = 'Finalizado' AND rated = 0 LIMIT 1")
    fun observeFinishedWalkForOwner(ownerEmail: String): Flow<walkerRequestEntity?>

    @Query("UPDATE walkerRequests SET rated = 1 WHERE id = :id")
    suspend fun markAsRated(id: Int)

    @Query("UPDATE walkerRequests SET rated = 1 WHERE ownerEmail = :ownerEmail AND status = 'Finalizado'")
    suspend fun markWalkAsRated(ownerEmail: String)
}
