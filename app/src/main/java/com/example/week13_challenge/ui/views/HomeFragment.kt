package com.example.week13_challenge.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week13_challenge.Adapters.NotesAdapter
import com.example.week13_challenge.R
import com.example.week13_challenge.data.repository.NotesRepository
import com.example.week13_challenge.data.local.NoteDatabase
import com.example.week13_challenge.databinding.FragmentHomeBinding
import com.example.week13_challenge.ui.viewmodels.NotesViewModel
import com.example.week13_challenge.ui.viewmodels.NotesViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesRepository: NotesRepository
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Repository & ViewModel

        val database = NoteDatabase.getDatabase(requireContext())

        notesRepository = NotesRepository(database.noteDao())
        notesViewModel = ViewModelProvider(this, NotesViewModelFactory(notesRepository)).get(NotesViewModel::class.java)
        notesAdapter = NotesAdapter(
            onNoteChecked = {note, isChecked ->
                notesViewModel.updateNoteCompleted(note,isChecked)
            },
            onNoteClicked = {note ->
                val action = HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(note)
                findNavController().navigate(action)
            }
        )

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter
        }


        val swipeCallBack = object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean  = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = notesAdapter.currentList[viewHolder.absoluteAdapterPosition]

                // Show Confirmation
                MaterialAlertDialogBuilder(requireContext()).setTitle("Delete note").setMessage("Are you sure you want to delete this note?").setPositiveButton("Delete"){ _,_ ->
                    notesViewModel.deleteNote(note)
                }.setNeutralButton("Cancel"){_,_ ->
                    notesAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }.show()
            }
        }

        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.notesRecyclerView)


        // observe live data from viewmodel
        notesViewModel.allNotes.observe(viewLifecycleOwner){ notes ->
            Log.d("NoteApp","All Notes: $notes")

            notes.forEach { note ->
                Log.d("NoteApp","Note ID: ${note.id}, Title: ${note.title}, TimeStamp: ${note.timestamp}")
            }

            notesAdapter.submitList(notes)

            binding.emptyStateText.visibility = if(notes.isEmpty()) View.VISIBLE else View.GONE
        }

        // FAb click
        binding.addNoteFab.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(null)
            findNavController().navigate(action)
        }

        // Add Some test Dummy Data
        //notesViewModel.addDummyNotes()

        // Menu Provider for search  + filter

        requireActivity().addMenuProvider(object:MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_home,menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.queryHint = "Search notes"

                notesViewModel.searchResults.observe(viewLifecycleOwner){ filteredNotes ->
                    notesAdapter.submitList(filteredNotes)
                }

                searchView.setOnQueryTextListener(object :OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        notesViewModel.setSearchQuery("%${newText.orEmpty()}%")
                        return true
                    }

                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.action_filter -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToNodeFilterFragment()
                        findNavController().navigate(action)
                        true
                    }
                    else -> false
                }
            }

        },viewLifecycleOwner)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}