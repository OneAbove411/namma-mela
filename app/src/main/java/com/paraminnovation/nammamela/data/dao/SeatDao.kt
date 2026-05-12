package com.paraminnovation.nammamela.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraminnovation.nammamela.data.entity.SeatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeatDao {
    @Query("SELECT * FROM seats ORDER BY section, rowLabel, seatNumber")
    fun observeAll(): Flow<List<SeatEntity>>

    @Query("SELECT * FROM seats")
    suspend fun getAll(): List<SeatEntity>

    @Query("SELECT COUNT(*) FROM seats")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(seats: List<SeatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(seat: SeatEntity)

    @Query("UPDATE seats SET status = :status, updatedAt = :ts WHERE id IN (:ids)")
    suspend fun setStatus(ids: List<String>, status: String, ts: Long = System.currentTimeMillis())

    @Query("UPDATE seats SET status = 'available', updatedAt = :ts")
    suspend fun resetAll(ts: Long = System.currentTimeMillis())
}
