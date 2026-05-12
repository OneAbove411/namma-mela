package com.paraminnovation.nammamela.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.paraminnovation.nammamela.data.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Query("SELECT * FROM tickets ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<TicketEntity?>

    @Insert
    suspend fun insert(t: TicketEntity): Long
}
