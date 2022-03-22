package com.example.cvoflashcardsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView flashcardQuestion;
    TextView flashcardAnswer;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ID's for flashcards elements
        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();


        if (allFlashcards != null && allFlashcards.size() > 0) {
            flashcardQuestion.setText(allFlashcards.get(0).getQuestion());
            flashcardAnswer.setText(allFlashcards.get(0).getAnswer());
        }


        //OnClick listener for flashcard question
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);

            }

        });
        //OnClick listener for flashcard answer
        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setVisibility(View.VISIBLE);
                flashcardAnswer.setVisibility(View.INVISIBLE);

            }

        });

        //OnClick listener for add button
        findViewById(R.id.flashcard_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                //MainActivity.this.startActivity(intent);
                startActivityForResult(intent, 100);
            }
        });

        //OnClick Listener for next button
        findViewById(R.id.flashcard_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't try to go to next card if you have no cards to begin with
                if (allFlashcards.size() == 0)
                    return;

                //Randomly choose the next card and ensure the current card won't be chosen
                int nextCard = getRandomNumber(0,allFlashcards.size());
                while(nextCard == currentCardDisplayedIndex){
                    nextCard = getRandomNumber(0,allFlashcards.size());
                }
                currentCardDisplayedIndex = nextCard;

                // set the question and answer TextViews with data from the database
                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);

                flashcardQuestion.setText(flashcard.getQuestion());
                flashcardAnswer.setText(flashcard.getAnswer());
            }
        });

        //OnClickListener for trash button
        findViewById(R.id.flashcard_trash_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(flashcardQuestion.getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
                currentCardDisplayedIndex--;

                if (currentCardDisplayedIndex < 0 && allFlashcards.size() > 0){
                    currentCardDisplayedIndex = 0;
                    Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);
                    flashcardQuestion.setText(flashcard.getQuestion());
                    flashcardAnswer.setText(flashcard.getAnswer());


                } else if(currentCardDisplayedIndex < 0){
                    String prompt = getString(R.string.empty_state);
                    flashcardQuestion.setText(prompt);
                    flashcardAnswer.setText(prompt);

                } else{
                    Flashcard flashcard = allFlashcards.get(currentCardDisplayedIndex);
                    flashcardQuestion.setText(flashcard.getQuestion());
                    flashcardAnswer.setText(flashcard.getAnswer());

                }
            }

        });

    } // OnCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if(data != null){
                String questionString = data.getExtras().getString("QUESTION_KEY");
                String answerString = data.getExtras().getString("ANSWER_KEY");
                TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
                TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
                flashcardQuestion.setText(questionString);
                flashcardAnswer.setText(answerString);

                flashcardDatabase.insertCard(new Flashcard(questionString, answerString));
                allFlashcards = flashcardDatabase.getAllCards();

            }

        }
    }
    //function to randomize card
    public int getRandomNumber(int minNumber, int maxNumber){
        Random rand = new Random();
        return rand.nextInt(maxNumber - minNumber) ;
    }



}