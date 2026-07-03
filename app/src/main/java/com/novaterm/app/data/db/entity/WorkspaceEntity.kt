package com.novaterm.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workspaces")
data class WorkspaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: Int,
    val iconName: String = "terminal",
    val currentDirectory: String = "",
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
