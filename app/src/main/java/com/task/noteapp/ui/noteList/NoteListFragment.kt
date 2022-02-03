package com.task.noteapp.ui.noteList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.task.noteapp.R
import com.task.noteapp.adapter.NotesAdapter
import com.task.noteapp.data.model.Note
import com.task.noteapp.databinding.FragmentNoteListBinding
import com.task.noteapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class NoteListFragment : Fragment(R.layout.fragment_note_list), NotesAdapter.OnItemClickListener {

    private val viewModel: NoteListViewModel by viewModels()
    private lateinit var binding: FragmentNoteListBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteListBinding.bind(view)
        val notesAdapter = NotesAdapter(this)
        binding.apply {
            buttonAddNote.setOnClickListener {
                findNavController().navigate(
                    NoteListFragmentDirections.actionNoteListFragmentToNoteAddFragment()
                )
            }
            recyclerViewNotes.apply {
                adapter = notesAdapter
                setHasFixedSize(true)
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = notesAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onNoteSwiped(note)
                }
            }).attachToRecyclerView(recyclerViewNotes)
        }

        viewModel.getAllNotes().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Collections.reverse(it)
                binding.containerEmptyNote.root.visibility = View.GONE
                notesAdapter.submitList(it)
            } else {
                binding.containerEmptyNote.root.visibility = View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.savedNoteEvent.collect { event ->
                when (event) {
                    is NoteListViewModel.SavedNoteEvent.ShowUndoDeleteNoteMessage -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.note_deleted),
                            Snackbar.LENGTH_LONG
                        ).setAction(getString(R.string.undo)) {
                            viewModel.onUndoDeleteClick(event.note)
                            notesAdapter.notifyDataSetChanged()
                        }.show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? MainActivity)?.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.notes)
        }
    }

    override fun onItemClick(note: Note) {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_NOTE, note)
        findNavController().navigate(R.id.action_noteListFragment_to_noteAddFragment, bundle)
    }

    companion object {
        const val BUNDLE_NOTE = "bundle_note"
    }
}