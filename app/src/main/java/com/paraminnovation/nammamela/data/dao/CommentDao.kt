package com.paraminnovation.nammamela.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.paraminnovation.nammamela.data.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<CommentEntity>>

    @Query("SELECT COUNT(*) FROM comments")
    suspend fun count(): Int

    @Insert
    suspend fun insert(comment: CommentEntity): Long

    @Query("DELETE FROM comments WHERE id = :id")
    suspend fun deleteById(id: Long)
}
