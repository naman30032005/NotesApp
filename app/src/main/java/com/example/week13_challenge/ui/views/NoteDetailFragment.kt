package com.example.week13_challenge.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.week13_challenge.data.local.NoteDatabase
import com.example.week13_challenge.data.repository.NotesRepository
import com.example.week13_challenge.databinding.FragmentNoteDetailBinding
import com.example.week13_challenge.domain.Note
import com.example.week13_challenge.domain.Priority
import com.example.week13_challenge.ui.viewmodels.NotesViewModel
import com.example.week13_challenge.ui.viewmodels.NotesViewModelFactory


class NoteDetailFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!


    private var isEditNote = false
    private var currentNote: Note? = null


    private lateinit var notesRepository: NotesRepository
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = NoteDatabase.getDatabase(requireContext())
        notesRepository = NotesRepository(db.noteDao())
        notesViewModel = ViewModelProvider(this, NotesViewModelFactory(notesRepository)).get(NotesViewModel::class.java)

        val args: NoteDetailFragmentArgs by navArgs()

        currentNote = args.note

        if (currentNote != null){
            populateFields(currentNote!!)
            setEditNote(false)
        } else{
            // Adding a new note
            currentNote = Note(
                id = 0,
                title = "",
                content = "",
                timestamp = System.currentTimeMillis(),
                completed = false,
                priority = Priority.Low
            )
            setEditNote(true)
        }

        binding.fabEditNote.setOnClickListener{
            setEditNote(true)
        }

        binding.saveButton.setOnClickListener {
            saveNote()
            setEditNote(false)
            findNavController().popBackStack()
        }
    }

    private fun populateFields(note: Note){
        binding.titleEdit.setText(note.title)
        binding.contentEdit.setText(note.content)

        val spinnerIndex = when(note.priority){
            Priority.None -> 0
            Priority.Low -> 1
            Priority.Medium -> 2
            Priority.High -> 3
        }
        binding.prioritySpinner.setSelection(spinnerIndex)
        binding.completedCheckBox.isChecked = note.completed
    }

    private fun setEditNote(enabled: Boolean){
        isEditNote = enabled

        binding.titleEdit.isEnabled = enabled
        binding.contentEdit.isEnabled = enabled
        binding.prioritySpinner.isEnabled = enabled
        binding.completedCheckBox.isEnabled = enabled

        // show the save button only in edit mode
        binding.saveButton.visibility = if (enabled) View.VISIBLE else View.GONE

        binding.fabEditNote.visibility = if(enabled) View.GONE else View.VISIBLE
    }

    private fun saveNote(){
        if (currentNote == null) return

        val pos = binding.prioritySpinner.selectedItemPosition
        val mappedPriority = when(pos){
            0 -> Priority.None
            1 -> Priority.Low
            2 -> Priority.Medium
            3 -> Priority.High
            else -> Priority.None
        }

        val updated = currentNote!!.copy(
            title = binding.titleEdit.text.toString(),
            content = binding.contentEdit.text.toString(),
            priority = mappedPriority,
            completed = binding.completedCheckBox.isChecked,
            timestamp = System.currentTimeMillis()
        )

        Log.d("NoteApp","priority: ${mappedPriority}")

        if (updated.id.toInt() == 0){
            notesViewModel.insertNote(updated)
        }
        else{
            notesViewModel.updateNote(updated)
        }

        currentNote = updated
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}