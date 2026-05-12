package com.paraminnovation.nammamela.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
