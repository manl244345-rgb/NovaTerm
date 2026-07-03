package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.DownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads ORDER BY createdAt DESC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM downloads WHERE status = :status ORDER BY createdAt DESC")
    fun getDownloadsByStatus(status: String): Flow<List<DownloadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadEntity)

    @Update
    suspend fun updateDownload(download: DownloadEntity)

    @Query("DELETE FROM downloads WHERE id = :id")
    suspend fun deleteDownload(id: String)

    @Query("DELETE FROM downloads WHERE status = 'completed'")
    suspend fun clearCompleted()
}
