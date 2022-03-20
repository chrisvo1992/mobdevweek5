package com.example.cvoflashcardsapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Flashcard.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract com.example.cvoflashcardsapp.FlashcardDao flashcardDao();
}
