package com.novaterm.app.di

import android.content.Context
import androidx.room.Room
import com.novaterm.app.data.db.NovaTermDatabase
import com.novaterm.app.data.db.dao.*
import com.novaterm.app.terminal.ShellExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNovaTermDatabase(@ApplicationContext context: Context): NovaTermDatabase {
        return Room.databaseBuilder(
            context,
            NovaTermDatabase::class.java,
            "novaterm.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWorkspaceDao(db: NovaTermDatabase): WorkspaceDao = db.workspaceDao()

    @Provides
    @Singleton
    fun provideProjectDao(db: NovaTermDatabase): ProjectDao = db.projectDao()

    @Provides
    @Singleton
    fun provideSshHostDao(db: NovaTermDatabase): SshHostDao = db.sshHostDao()

    @Provides
    @Singleton
    fun provideDownloadDao(db: NovaTermDatabase): DownloadDao = db.downloadDao()

    @Provides
    @Singleton
    fun provideNoteDao(db: NovaTermDatabase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideTerminalHistoryDao(db: NovaTermDatabase): TerminalHistoryDao = db.terminalHistoryDao()

    @Provides
    @Singleton
    fun provideBookmarkDao(db: NovaTermDatabase): BookmarkDao = db.bookmarkDao()

    @Provides
    @Singleton
    fun provideGitRepoDao(db: NovaTermDatabase): GitRepoDao = db.gitRepoDao()

    @Provides
    @Singleton
    fun provideShellExecutor(): ShellExecutor = ShellExecutor()
}
