package com.example.week13_challenge.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.week13_challenge.data.repository.NotesRepository
import com.example.week13_challenge.domain.Note
import com.example.week13_challenge.domain.Priority
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val allNotes : LiveData<List<Note>> = repository.getAllNotes().asLiveData()

    fun updateNoteCompleted(note: Note, isChecked: Boolean){
        viewModelScope.launch {
            repository.updateCompleted(note,isChecked)
        }
    }

    fun insertNote(note:Note){
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun updateNote(note:Note){
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note:Note){
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    private val _searchQuery = MutableLiveData<String>()

    val searchResults : LiveData<List<Note>> = _searchQuery.switchMap { query ->
        repository.searchNotes(query).asLiveData()
    }

    fun setSearchQuery(query:String){
        _searchQuery.value = query
    }

    fun sortByDate():LiveData<List<Note>>{
        return allNotes.map {
                notes ->
            notes.sortedByDescending { it.timestamp }
        }
    }

    fun sortByPriority(): LiveData<List<Note>>{
        return allNotes.map{
                notes ->
            notes.sortedBy { it.priority.ordinal }
        }
    }

    fun filterNotes(title:String?, content: String?, completed : Boolean?, priority: Priority?):LiveData<List<Note>>{
        return repository.filterNotes(title,content, completed, priority).asLiveData()
    }

    fun addDummyNotes(){
        val dummyNotes = listOf(
            Note(title = "Buy Groceries", content = "Milk, Eggs, Bread", priority = Priority.High),
            Note(title = "Walk the dog", content = "Evening Walk", priority = Priority.Medium),
            Note(title = "Read Book", content = "Finish Kotlin Book", priority = Priority.Low),
            Note(title = "Locker Code", content = "123456789", priority = Priority.None),
        )

        viewModelScope.launch {
            dummyNotes.forEach { repository.insertNote(it) }
        }
    }
}