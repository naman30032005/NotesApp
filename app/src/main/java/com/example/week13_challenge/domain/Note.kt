package com.example.week13_challenge.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 *  Domain model representing the note itself within the app
 *
 *  This is the version of the note that the UI and viewmodel work with
 *  It is distinct from [NoteEntity] (the database model) to seperation of concerns
 *
 *  @property id Unique identifier for the note
 *  @property title Short description/title for the note
 *  @property content The actual content of the note
 *  @property timestamp Used to store Creation or last modification date
 *  @property completed For reminding the completion of a task
 *  @property priority Stores the priority of the note
 * */

@Parcelize
data class Note(
    val id: Long = 0L,
    var title: String,
    var content: String,
    var timestamp:Long = System.currentTimeMillis(),
    var completed:Boolean = false,
    var priority: Priority = Priority.None
) : Parcelable