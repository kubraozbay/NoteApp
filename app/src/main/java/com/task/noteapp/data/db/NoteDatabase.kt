package com.task.noteapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.noteapp.data.model.Note
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    class Callback @Inject constructor(
        private val database: Provider<NoteDatabase>
    ) : RoomDatabase.Callback()
}