package ca.thewizards.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ca.thewizards.flashcards.Model.Question;

public class PlayActivity extends AppCompatActivity {

    private FlashcardsApplication application;
    private List<Question> questions;
    private TextInputEditText txt_answer;

    private ImageView imageViewFace;
    private Button btn_ok;

    private boolean animation;
    private int collectionId;
    private int curQuestion = 0;
    private CardView cardView;
    private TextView txt_question;

    private int totalQuestion;
    private int totalCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("darkTheme", false)){
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }

        animation = intent.getBooleanExtra("animation", false);
        collectionId = intent.getIntExtra("collectionId", 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        application = (FlashcardsApplication)getApplication();
        questions = application.getQuestions(collectionId);

        // Test
        Question q1 = new Question(1, "red", "What color is apple?", 1);
        Question q2 = new Question(2, "yellow", "What color is banana?", 1);
        questions.add(q1);
        questions.add(q2);

        totalQuestion = questions.size();

        if (totalQuestion > 0){
            txt_answer = findViewById(R.id.txt_answer);
            txt_question = findViewById(R.id.txt_question);

            imageViewFace = findViewById(R.id.image_Face);
            btn_ok = findViewById(R.id.btn_ok);
            cardView = findViewById(R.id.question_card_view);

            btn_ok.setOnClickListener(handleClick());
            txt_question.setText(questions.get(curQuestion).getQuestion());
        }
    }

    View.OnClickListener handleClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayActivity.this, "button clicked", Toast.LENGTH_SHORT).show();

                // Animation effect
                if (animation){
                    cardView.animate().withLayer()
                            .rotationY(90)
                            .setDuration(300)
                            .withEndAction(
                                    new Runnable() {
                                        @Override public void run() {
                                            txt_question.setText(questions.get(curQuestion).getAnswer());

                                            // second quarter turn
                                            cardView.setRotationY(-90);
                                            cardView.animate().withLayer()
                                                    .rotationY(0)
                                                    .setDuration(1000)
                                                    .start();
                                        }
                                    }
                            ).start();
                }
                else {
                    txt_question.setText(questions.get(curQuestion).getAnswer());
                }

                // happy face and sad face
                String answerInput = txt_answer.getText().toString().toLowerCase();

                // TODO: check correct answer from database
                String answerCorrect = "right".toLowerCase(); // test

                Toast.makeText(PlayActivity.this, answerInput + "=" + answerCorrect, Toast.LENGTH_SHORT).show();

                if(answerCorrect.equals(answerInput)){
                    imageViewFace.setImageResource(R.drawable.ic_happy_face);
                }
                else{
                    imageViewFace.setImageResource(R.drawable.ic_sad_face);
                }


                // display the result

                // skip to next question
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}