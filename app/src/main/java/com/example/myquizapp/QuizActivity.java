    package com.example.myquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

    public class QuizActivity extends AppCompatActivity {
        private TextView textViewQuestion;
        private TextView textViewScore;
        private TextView textViewQuestionCount;
        private TextView textViewCountDown;
        private RadioGroup rbGroup;
        private RadioButton rb1;
        private RadioButton rb2;
        private RadioButton rb3;
        private Button buttonConfirmNext;

        private List<Question> questionList;

        private ColorStateList textColorDefault;
        private int questionCounter;
        private int questionCountTotal;
        private Question currentQuestion;
        public static final String EXTRA_SCORE="ESdsfsaf";


        private int score;
        private boolean answered;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quiz);


            textViewQuestion = findViewById(R.id.text_view_question);
            textViewScore = findViewById(R.id.text_view_score);
            textViewQuestionCount = findViewById(R.id.text_view_question_count);
            textViewCountDown = findViewById(R.id.text_view_countdown);
            rbGroup = findViewById(R.id.radio_group);
            rb1 = findViewById(R.id.radio_button1);
            rb2 = findViewById(R.id.radio_button2);
            rb3 = findViewById(R.id.radio_button3);
            buttonConfirmNext = findViewById(R.id.button_confirm_next);
            textColorDefault = rb1.getTextColors();


            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getAllQuestions();

            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();

            buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!answered) {
                        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                            checkAnswer();

                        } else
                            Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    } else
                        showNextQuestion();

                }

            });

        }

        private void showNextQuestion() {
            rb1.setTextColor(textColorDefault);
            rb2.setTextColor(textColorDefault);
            rb3.setTextColor(textColorDefault);
            rbGroup.clearCheck();

            if (questionCounter < questionCountTotal) {
                currentQuestion = questionList.get(questionCounter);
                textViewQuestion.setText(currentQuestion.getQuestion());
                rb1.setText(currentQuestion.getOption1());
                rb2.setText(currentQuestion.getOption2());
                rb3.setText(currentQuestion.getOption3());
                questionCounter++;
                textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
                answered = false;
                buttonConfirmNext.setText("Confirm");


            } else {
                finishQuiz();

            }
        }

        private void checkAnswer() {
            answered = true;

            RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
            int answer = rbGroup.indexOfChild(rbSelected) + 1;
            if (answer == currentQuestion.getAnswerNr()) {
                score++;
                textViewScore.setText("Score: " + score);
            }
            showSolution();
        }

        private void showSolution() {
            rb1.setTextColor(Color.RED);
            rb2.setTextColor(Color.RED);
            rb3.setTextColor(Color.RED);

            if (currentQuestion.getAnswerNr() == 1) {
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 1 is correct");
            } else if (currentQuestion.getAnswerNr() == 2) {
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 2 is correct");

            } else {
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 3 is correct");

            }
            if(questionCounter<questionCountTotal)
                buttonConfirmNext.setText("NEXT");
            else
                buttonConfirmNext.setText("Finish");
        }

        private void finishQuiz(){
            Intent rintent=new Intent();
            rintent.putExtra(EXTRA_SCORE,score);
            setResult(RESULT_OK);

                finish();


            }

        }

