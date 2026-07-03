package com.novaterm.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val destinationPath: String,
    val totalBytes: Long = 0L,
    val downloadedBytes: Long = 0L,
    val status: String = "pending",
    val mimeType: String = "",
    val checksum: String = "",
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
