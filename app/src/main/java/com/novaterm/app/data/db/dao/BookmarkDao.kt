package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY isFavorite DESC, name ASC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Update
    suspend fun updateBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: String)
}
