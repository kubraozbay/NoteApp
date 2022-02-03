package com.task.noteapp.ui.noteAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.data.NoteRepository
import com.task.noteapp.data.model.Note
import com.task.noteapp.ui.noteList.NoteListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteAddViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    fun saveOrUpdateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.insertOrUpdateNote(note)
        }
    }
}