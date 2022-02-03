package com.task.noteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.task.noteapp.data.model.Note
import com.task.noteapp.databinding.ItemNoteBinding
import com.task.noteapp.util.getBitmapFromByteArray

class NotesAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Note, NotesAdapter.NoteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val note = getItem(position)
                        listener.onItemClick(note)
                    }
                }
            }
        }

        fun bind(note: Note) {
            binding.apply {
                textViewTitle.text = note.title
                textViewContent.text = note.content
                textViewDate.text = note.date
                if (note.isUpdated == true) {
                    textViewUpdated.visibility = View.VISIBLE
                } else {
                    textViewUpdated.visibility = View.GONE
                }
                if (note.imageUrl != null) {
                    imageView.visibility = View.VISIBLE
                    imageView.setImageBitmap(note.imageUrl.getBitmapFromByteArray())
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
}