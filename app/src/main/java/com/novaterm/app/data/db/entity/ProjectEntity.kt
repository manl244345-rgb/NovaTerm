package com.novaterm.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val language: String,
    val path: String,
    val workspaceId: String,
    val description: String = "",
    val gitRepoId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis(),
    val lastOpenedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false
)
