package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects WHERE isDeleted = 0 ORDER BY lastOpenedAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE workspaceId = :workspaceId AND isDeleted = 0 ORDER BY lastOpenedAt DESC")
    fun getProjectsByWorkspace(workspaceId: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("UPDATE projects SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteProject(id: String)
}
