package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.WorkspaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkspaceDao {
    @Query("SELECT * FROM workspaces WHERE isDeleted = 0 ORDER BY lastModifiedAt DESC")
    fun getAllWorkspaces(): Flow<List<WorkspaceEntity>>

    @Query("SELECT * FROM workspaces WHERE id = :id")
    suspend fun getWorkspaceById(id: String): WorkspaceEntity?

    @Query("SELECT * FROM workspaces WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveWorkspace(): WorkspaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkspace(workspace: WorkspaceEntity)

    @Update
    suspend fun updateWorkspace(workspace: WorkspaceEntity)

    @Query("UPDATE workspaces SET isActive = 0")
    suspend fun deactivateAllWorkspaces()

    @Query("UPDATE workspaces SET isActive = 1 WHERE id = :id")
    suspend fun activateWorkspace(id: String)

    @Query("UPDATE workspaces SET isDeleted = 1, lastModifiedAt = :timestamp WHERE id = :id")
    suspend fun softDeleteWorkspace(id: String, timestamp: Long = System.currentTimeMillis())
}
