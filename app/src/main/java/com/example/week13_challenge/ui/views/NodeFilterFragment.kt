package com.example.week13_challenge.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week13_challenge.Adapters.NotesAdapter
import com.example.week13_challenge.data.local.NoteDatabase
import com.example.week13_challenge.data.repository.NotesRepository
import com.example.week13_challenge.databinding.FragmentNodeFilterBinding
import com.example.week13_challenge.domain.Priority
import com.example.week13_challenge.ui.viewmodels.NotesViewModel
import com.example.week13_challenge.ui.viewmodels.NotesViewModelFactory

class NodeFilterFragment : Fragment() {

    private var _binding: FragmentNodeFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter

    private val notesViewModel: NotesViewModel by activityViewModels{
        NotesViewModelFactory(NotesRepository(
            NoteDatabase.getDatabase(requireContext()).noteDao()
        )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNodeFilterBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesAdapter = NotesAdapter(
            onNoteChecked = {
                    note,isChecked -> notesViewModel.updateNoteCompleted(note,isChecked)
            },
            onNoteClicked = {
                    note -> // navigate maybe
            }
        )

        binding.filteredNotesRecyclerView.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter
        }

        notesViewModel.allNotes.observe(viewLifecycleOwner, Observer {
                notes -> notesAdapter.submitList(notes)
        })

        binding.sortByDateBtn.setOnClickListener {
            notesViewModel.sortByDate().observe(viewLifecycleOwner){
                    sortedNotes -> notesAdapter.submitList(sortedNotes)
            }
        }

        binding.sortByPriorityBtn.setOnClickListener {
            notesViewModel.sortByPriority().observe(viewLifecycleOwner){
                    sortedNotes -> notesAdapter.submitList(sortedNotes)
            }
        }

        binding.applyFilterBtn.setOnClickListener {
            val title = binding.titleFilterEdit.text.toString().takeIf { it.isNotBlank() }
            val content = binding.contentFilterEdit.text.toString().takeIf { it.isNotBlank() }
            val completed = when{
                binding.completedFilterCheckBox.isChecked -> true
                else -> null
            }

            val priority: Priority? = when(binding.priorityFilterSpinner.selectedItemPosition){
                0 -> Priority.None
                1 -> Priority.Low
                2 -> Priority.Medium
                3 -> Priority.High
                4 -> null
                else -> null
            }

            notesViewModel.filterNotes(title, content, completed, priority).observe(viewLifecycleOwner){
                    notes ->
                notesAdapter.submitList(notes)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}