package ca.thewizards.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ca.thewizards.flashcards.Model.Question;

public class PlayActivity extends AppCompatActivity {

    private FlashcardsApplication application;
    private List<Question> questions;
    private TextInputEditText txt_answer;
    private Button btn_ok;
    private boolean animation;
    private int collectionId;
    private int curQuestion = 0;
    private CardView cardView;
    private TextView txt_question;

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

        if (questions.size() > 0){
            txt_answer = findViewById(R.id.txt_answer);
            txt_question = findViewById(R.id.txt_question);
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