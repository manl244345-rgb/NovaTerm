package com.novaterm.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terminal_history")
data class TerminalHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val command: String,
    val workspaceId: String = "",
    val sessionId: String = "",
    val exitCode: Int = 0,
    val isFavorite: Boolean = false,
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
