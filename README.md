# NotesApp

NotesApp is a native Android notes application built with Kotlin. The project was created as part of my Android development learning journey, with a focus on app navigation, local data storage, RecyclerView lists, and clean project structure.

This project is currently a student portfolio project and is being improved as I continue learning Android development.

## Project Overview

The app allows users to create, view, edit, complete, search, filter, sort, and delete notes. Notes are stored locally on the device using Room Database.

The main purpose of this project is to practise building a complete Android app using common Android development components such as Navigation Graphs, ViewModels, RecyclerView, Room, and ViewBinding.

## Features

- Create new notes
- View saved notes in a RecyclerView list
- Edit existing notes
- Mark notes as completed
- Delete notes using swipe actions
- Search notes by title or content
- Filter notes by title, content, completion status, and priority
- Sort notes by date
- Sort notes by priority
- Store notes locally using Room Database
- Navigate between screens using the Android Navigation Component

## Tech Stack

- Kotlin
- Android Studio
- Android Navigation Component
- Safe Args
- RecyclerView
- ListAdapter
- DiffUtil
- Room Database
- Kotlin Flow
- LiveData
- ViewModel
- ViewBinding
- Material Components
- Gradle Kotlin DSL

## App Structure

The project is organised into separate areas for data, domain, UI, and adapters.

```text
app/src/main/java/com/example/week13_challenge
├── Adapters
│   ├── NoteDiffCallback.kt
│   └── NotesAdapter.kt
├── data
│   ├── local
│   │   ├── Converters.kt
│   │   ├── NoteDao.kt
│   │   ├── NoteDatabase.kt
│   │   └── NoteEntity.kt
│   └── repository
│       └── NotesRepository.kt
├── domain
│   ├── Note.kt
│   └── Priority.kt
├── ui
│   ├── viewmodels
│   │   ├── NotesViewModel.kt
│   │   └── NotesViewModelFactory.kt
│   └── views
│       ├── HomeFragment.kt
│       ├── NoteDetailFragment.kt
│       ├── NoteFilterFragment.kt
│       └── SplashScreenFragment.kt
└── MainActivity.kt
