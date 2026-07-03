package com.novaterm.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ssh_hosts")
data class SshHostEntity(
    @PrimaryKey val id: String,
    val nickname: String,
    val hostname: String,
    val port: Int = 22,
    val username: String,
    val authMethod: String = "password",
    val privateKeyId: String? = null,
    val notes: String = "",
    val tags: String = "",
    val colorLabel: String = "",
    val isFavorite: Boolean = false,
    val groupName: String = "",
    val lastConnectedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)
