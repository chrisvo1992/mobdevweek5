package com.example.cvoflashcardsapp;

import android.content.Context;

import androidx.room.Room;

import com.example.cvoflashcardsapp.AppDatabase;

import java.util.List;

public class FlashcardDatabase {
    private final AppDatabase db;

    FlashcardDatabase(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "flashcard-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public void initFirstCard() {
        if (db.flashcardDao().getAll().isEmpty()) {
            insertCard(new com.example.cvoflashcardsapp.Flashcard("Who is the 44th President of the United States", "Barack Obama"));
        }
    }

    public List<com.example.cvoflashcardsapp.Flashcard> getAllCards() {
        return db.flashcardDao().getAll();
    }

    public void insertCard(com.example.cvoflashcardsapp.Flashcard flashcard) {
        db.flashcardDao().insertAll(flashcard);
    }

    public void deleteCard(String flashcardQuestion) {
        List<com.example.cvoflashcardsapp.Flashcard> allCards = db.flashcardDao().getAll();
        for (com.example.cvoflashcardsapp.Flashcard f : allCards) {
            if (f.getQuestion().equals(flashcardQuestion)) {
                db.flashcardDao().delete(f);
            }
        }
    }

    public void updateCard(com.example.cvoflashcardsapp.Flashcard flashcard) {
        db.flashcardDao().update(flashcard);
    }

    public void deleteAll() {
        for (com.example.cvoflashcardsapp.Flashcard f : db.flashcardDao().getAll()) {
            db.flashcardDao().delete(f);
        }
    }
}
