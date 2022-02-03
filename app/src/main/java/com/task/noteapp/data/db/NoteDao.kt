package com.task.noteapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.task.noteapp.data.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table")
    fun getNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Delete
    suspend fun delete(note: Note)

}