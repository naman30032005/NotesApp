package com.example.week13_challenge.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.week13_challenge.databinding.ItemNoteBinding
import com.example.week13_challenge.domain.Note
import com.example.week13_challenge.domain.Priority
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NotesAdapter(
    private var onNoteChecked: (Note, Boolean) -> Unit,
    private var onNoteClicked: (Note) -> Unit
): ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    inner class NoteViewHolder(private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note:Note){
            binding.apply {
                textTitle.text = note.title
                textTimestamp.text = formatTimestamp(note.timestamp)
                textPriority.text = note.priority.name

                checkCompleted.setOnClickListener(null)
                checkCompleted.isChecked = note.completed

                val priorityColor = when(note.priority){
                    Priority.High -> Color.RED
                    Priority.Medium -> Color.BLUE
                    Priority.Low -> Color.GRAY
                    Priority.None -> Color.BLACK
                }

                textPriority.setTextColor(priorityColor)
                textTitle.setTextColor(priorityColor)
                textTimestamp.setTextColor(priorityColor)
                checkCompleted.setTextColor(priorityColor)

                checkCompleted.setOnCheckedChangeListener{ _, isChecked ->
                    onNoteChecked(note,isChecked)
                }

                root.setOnClickListener {
                    onNoteClicked(note)
                }

            }
        }


        private fun formatTimestamp(timestamp:Long):String{
            val date = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            return date.format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}