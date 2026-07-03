package com.novaterm.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "git_repos")
data class GitRepoEntity(
    @PrimaryKey val id: String,
    val name: String,
    val localPath: String,
    val remoteUrl: String = "",
    val currentBranch: String = "main",
    val workspaceId: String = "",
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis()
)
