package com.task.noteapp.ui.noteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.data.NoteRepository
import com.task.noteapp.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val savedNoteEventChannel = Channel<SavedNoteEvent>()
    val savedNoteEvent = savedNoteEventChannel.receiveAsFlow()

    fun getAllNotes() = noteRepository.getAllNotes()

    fun onNoteSwiped(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
            savedNoteEventChannel.send(SavedNoteEvent.ShowUndoDeleteNoteMessage(note))
        }
    }

    fun onUndoDeleteClick(note: Note) {
        viewModelScope.launch {
            noteRepository.insertOrUpdateNote(note)
        }
    }

    sealed class SavedNoteEvent {
        data class ShowUndoDeleteNoteMessage(val note: Note) : SavedNoteEvent()
    }
}