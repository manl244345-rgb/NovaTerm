package com.novaterm.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.novaterm.app.data.db.dao.*
import com.novaterm.app.data.db.entity.*

@Database(
    entities = [
        WorkspaceEntity::class,
        ProjectEntity::class,
        SshHostEntity::class,
        DownloadEntity::class,
        NoteEntity::class,
        TerminalHistoryEntity::class,
        BookmarkEntity::class,
        GitRepoEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class NovaTermDatabase : RoomDatabase() {
    abstract fun workspaceDao(): WorkspaceDao
    abstract fun projectDao(): ProjectDao
    abstract fun sshHostDao(): SshHostDao
    abstract fun downloadDao(): DownloadDao
    abstract fun noteDao(): NoteDao
    abstract fun terminalHistoryDao(): TerminalHistoryDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun gitRepoDao(): GitRepoDao
}
