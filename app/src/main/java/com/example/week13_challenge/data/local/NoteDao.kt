package com.example.week13_challenge.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(" Select * from notes ORDER BY timestamp Desc ")
    fun getAllNotes(): Flow<List<NoteEntity>>

    // Update the completed flag for the note without updating the whole entity
    @Query("Update notes Set completed = :isCompleted Where id = :noteid")
    suspend fun updateCompleted(noteid: Long, isCompleted: Boolean)

    @Query("Select * from notes Where title like :query or content like :query")
    fun searchNotes(query: String): Flow<List<NoteEntity>>


    @Query("""
        Select * from notes
        Where (:title IS NULL or title like '%' || :title || '%')
        And(:content IS NULL OR content like '%' || :content || '%')
        And(:completed IS NULL or completed= :completed)
        And(:priority IS NULL or priority= :priority)
    """)
    fun filterNotes(title:String?, content: String?, completed : Boolean?, priority: String?): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)
}