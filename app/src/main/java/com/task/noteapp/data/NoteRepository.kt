package com.task.noteapp.data

import com.task.noteapp.data.db.NoteDao
import com.task.noteapp.data.model.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNotes() = noteDao.getNotes()

    suspend fun insertOrUpdateNote(note: Note) = noteDao.insert(note)

    suspend fun deleteNote(note: Note) = noteDao.delete(note)
}