package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.SshHostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SshHostDao {
    @Query("SELECT * FROM ssh_hosts WHERE isDeleted = 0 ORDER BY isFavorite DESC, nickname ASC")
    fun getAllHosts(): Flow<List<SshHostEntity>>

    @Query("SELECT * FROM ssh_hosts WHERE id = :id")
    suspend fun getHostById(id: String): SshHostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHost(host: SshHostEntity)

    @Update
    suspend fun updateHost(host: SshHostEntity)

    @Query("UPDATE ssh_hosts SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteHost(id: String)

    @Query("UPDATE ssh_hosts SET lastConnectedAt = :timestamp WHERE id = :id")
    suspend fun updateLastConnected(id: String, timestamp: Long = System.currentTimeMillis())
}
