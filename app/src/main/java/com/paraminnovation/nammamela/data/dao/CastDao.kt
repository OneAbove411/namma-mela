package com.paraminnovation.nammamela.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraminnovation.nammamela.data.entity.CastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CastDao {
    @Query("SELECT * FROM cast_info WHERE id = 1 LIMIT 1")
    fun observeCast(): Flow<CastEntity?>

    @Query("SELECT * FROM cast_info WHERE id = 1 LIMIT 1")
    suspend fun getCast(): CastEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cast: CastEntity)
}
