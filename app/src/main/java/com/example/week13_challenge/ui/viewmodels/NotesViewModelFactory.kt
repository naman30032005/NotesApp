package com.example.week13_challenge.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.week13_challenge.data.repository.NotesRepository

class NotesViewModelFactory(private val notesRepository: NotesRepository): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass:Class<T>):T{
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(notesRepository) as T
        }

        throw IllegalArgumentException(" Unknown Viewmodel class")
    }

}