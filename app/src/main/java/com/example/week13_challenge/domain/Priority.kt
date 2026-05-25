package com.example.week13_challenge.domain

/**
*
* This Enum represents the priority of the note in our app
*
* Defines the importance/urgency of a note, also shown in UI using color coding or sorting
* - [None] -> default
* - [Low]  -> non-urgent tasks (casual notes)
* - [Medium] -> medium level for normal notes
* - [High] -> Urgent or important reminders
* */


enum class Priority {
                    None,
                    Low,
                    Medium,
                    High
}