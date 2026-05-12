package com.paraminnovation.nammamela.data.repository

import com.paraminnovation.nammamela.data.dao.TicketDao
import com.paraminnovation.nammamela.data.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketRepository(private val dao: TicketDao) {
    fun observeAll(): Flow<List<TicketEntity>> = dao.observeAll()
    fun observeById(id: Long): Flow<TicketEntity?> = dao.observeById(id)
    suspend fun insert(t: TicketEntity): Long = dao.insert(t)
}
