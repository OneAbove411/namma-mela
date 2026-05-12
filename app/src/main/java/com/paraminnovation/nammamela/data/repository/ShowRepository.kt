package com.paraminnovation.nammamela.data.repository

import com.paraminnovation.nammamela.data.dao.CastDao
import com.paraminnovation.nammamela.data.dao.ShowDao
import com.paraminnovation.nammamela.data.entity.CastEntity
import com.paraminnovation.nammamela.data.entity.ShowEntity
import kotlinx.coroutines.flow.Flow

class ShowRepository(
    private val showDao: ShowDao,
    private val castDao: CastDao
) {
    fun observeShow(): Flow<ShowEntity?> = showDao.observeShow()
    fun observeCast(): Flow<CastEntity?> = castDao.observeCast()

    suspend fun seedIfEmpty() {
        if (showDao.getShow() == null) showDao.upsert(ShowEntity())
        if (castDao.getCast() == null) castDao.upsert(CastEntity())
    }

    suspend fun updateShow(show: ShowEntity) = showDao.upsert(show)
    suspend fun updateCast(cast: CastEntity) = castDao.upsert(cast)
}
