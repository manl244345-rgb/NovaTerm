package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.TerminalHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TerminalHistoryDao {
    @Query("SELECT * FROM terminal_history ORDER BY createdAt DESC LIMIT 1000")
    fun getHistory(): Flow<List<TerminalHistoryEntity>>

    @Query("SELECT * FROM terminal_history WHERE isPinned = 1 ORDER BY createdAt DESC")
    fun getPinnedCommands(): Flow<List<TerminalHistoryEntity>>

    @Query("SELECT * FROM terminal_history WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteCommands(): Flow<List<TerminalHistoryEntity>>

    @Insert
    suspend fun insertHistory(entry: TerminalHistoryEntity): Long

    @Query("UPDATE terminal_history SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE terminal_history SET isPinned = :isPinned WHERE id = :id")
    suspend fun setPinned(id: Long, isPinned: Boolean)

    @Query("DELETE FROM terminal_history WHERE isPinned = 0 AND isFavorite = 0")
    suspend fun clearHistory()
}
