package com.task.noteapp.di

import android.app.Application
import androidx.room.Room
import com.task.noteapp.data.db.NoteDao
import com.task.noteapp.data.db.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: NoteDatabase.Callback): NoteDatabase {
        return Room.databaseBuilder(application, NoteDatabase::class.java, "note_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideNoteDao(db: NoteDatabase): NoteDao {
        return db.getNoteDao()
    }

}