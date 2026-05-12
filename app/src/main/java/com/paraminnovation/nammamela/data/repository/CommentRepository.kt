package com.paraminnovation.nammamela.data.repository

import com.paraminnovation.nammamela.data.dao.CommentDao
import com.paraminnovation.nammamela.data.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

class CommentRepository(private val commentDao: CommentDao) {

    fun observeAll(): Flow<List<CommentEntity>> = commentDao.observeAll()

    suspend fun seedIfEmpty() {
        if (commentDao.count() > 0) return
        val defaults = listOf(
            CommentEntity(authorName = "Suresh M", text = "What an incredible performance! The Ramayana was brought to life!"),
            CommentEntity(authorName = "Kavitha R", text = "Best drama troupe in Karnataka! My whole family loved it."),
            CommentEntity(authorName = "Prasad G", text = "Ganesh Rao as the comedian was hilarious! Can't stop laughing!"),
            CommentEntity(authorName = "Anitha K", text = "Lakshmi Devi's singing gave me goosebumps. Pure magic!")
        )
        defaults.forEach { commentDao.insert(it) }
    }

    suspend fun post(name: String, text: String) {
        commentDao.insert(CommentEntity(authorName = name, text = text))
    }
}
