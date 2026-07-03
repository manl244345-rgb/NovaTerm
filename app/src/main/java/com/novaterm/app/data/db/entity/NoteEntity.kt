package com.novaterm.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val workspaceId: String = "",
    val tags: String = "",
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis()
)
