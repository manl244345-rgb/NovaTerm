package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.GitRepoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GitRepoDao {
    @Query("SELECT * FROM git_repos WHERE isDeleted = 0 ORDER BY lastModifiedAt DESC")
    fun getAllRepos(): Flow<List<GitRepoEntity>>

    @Query("SELECT * FROM git_repos WHERE workspaceId = :workspaceId AND isDeleted = 0")
    fun getReposByWorkspace(workspaceId: String): Flow<List<GitRepoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(repo: GitRepoEntity)

    @Update
    suspend fun updateRepo(repo: GitRepoEntity)

    @Query("UPDATE git_repos SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteRepo(id: String)
}
