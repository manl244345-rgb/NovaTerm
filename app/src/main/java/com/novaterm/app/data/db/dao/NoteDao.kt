package com.novaterm.app.data.db.dao

import androidx.room.*
import com.novaterm.app.data.db.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY lastModifiedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE workspaceId = :workspaceId AND isDeleted = 0 ORDER BY lastModifiedAt DESC")
    fun getNotesByWorkspace(workspaceId: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("UPDATE notes SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteNote(id: String)
}
