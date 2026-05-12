package com.paraminnovation.nammamela.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraminnovation.nammamela.data.entity.ShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {
    @Query("SELECT * FROM show_info WHERE id = 1 LIMIT 1")
    fun observeShow(): Flow<ShowEntity?>

    @Query("SELECT * FROM show_info WHERE id = 1 LIMIT 1")
    suspend fun getShow(): ShowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(show: ShowEntity)
}
