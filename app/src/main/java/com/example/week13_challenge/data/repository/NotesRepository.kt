package com.example.week13_challenge.data.repository

import com.example.week13_challenge.data.local.NoteDao
import com.example.week13_challenge.data.local.NoteEntity
import com.example.week13_challenge.domain.Note
import com.example.week13_challenge.domain.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class NotesRepository(private val noteDao: NoteDao) {
    /**
     * Get all notes as a Flow of domain Notes object
     * */
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes().map{ entityList ->
        entityList.map { it.toNote() }
    }

    fun searchNotes(query: String): Flow<List<Note>> = noteDao.searchNotes(query).map { findList -> findList.map{ it.toNote() }
    }

    fun filterNotes(title:String?, content : String?, completed: Boolean?, priority: Priority?): Flow<List<Note>> = noteDao.filterNotes(title,content,completed,priority?.name).map{
            filteredList -> filteredList.map{it.toNote()}
    }

    suspend fun updateCompleted(note:Note, isCompleted:Boolean){
        noteDao.updateCompleted(note.id,isCompleted)
    }

    suspend fun insertNote(note: Note){
        noteDao.insertNote(note.toEntity())
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(note.toEntity())
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note.toEntity())
    }


    // Mapper function between Entity and Domain
    private fun NoteEntity.toNote():Note = Note(
        id = this.id,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp,
        completed = this.completed,
        priority = this.priority
    )

    private fun Note.toEntity():NoteEntity = NoteEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp,
        completed = this.completed,
        priority = this.priority
    )
}