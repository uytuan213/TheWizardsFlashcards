package ca.thewizards.flashcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import androidx.preference.PreferenceManager;

import ca.thewizards.flashcards.Model.Question;

public class PlayActivity extends AppCompatActivity {

    private FlashcardsApplication application;
    private List<Question> questions;
    private TextInputEditText txt_answer;

    private ImageView image_Face;
    private Button btn_ok;
    private TextView view_Result;
    private Button btn_next;

    private boolean animation;
    private int collectionId;
    private int questionIndex = 0;
    private CardView cardView;
    private TextView txt_question;

    private int totalQuestion;
    private int totalCorrect;

    private SharedPreferences sharedPref;
    private int faceFlag;   // 0: happy; 1: sad; -1: none
    private boolean nextBtnFlag;

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

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        application = (FlashcardsApplication)getApplication();
        questions = application.getQuestions(collectionId);

        // Test; TODO: remove later
//        Question q1 = new Question(1, "Red", "What color is apple?", 1);
//        Question q2 = new Question(2, "Yellow", "What color is banana?", 1);
//        Question q3 = new Question(3, "Blue", "What color is sky?", 1);
//        questions.add(q1);
//        questions.add(q2);
//        questions.add(q3);

        totalQuestion = questions.size();
        totalCorrect = 0;

        if (totalQuestion > 0){
            txt_answer = findViewById(R.id.txt_answer);
            txt_question = findViewById(R.id.txt_question);

            image_Face = findViewById(R.id.image_Face);
            btn_ok = findViewById(R.id.btn_ok);
            cardView = findViewById(R.id.question_card_view);
            btn_next = findViewById(R.id.btn_next);
            view_Result = findViewById(R.id.view_result);
            view_Result.setText("Result: " + totalCorrect + "/" + totalQuestion);

            btn_ok.setOnClickListener(handleClick());
            btn_next.setOnClickListener(handleClick());
            displayQuestion(txt_question, questionIndex);
        }
    }

    View.OnClickListener handleClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_ok:
                    // Animation effect and display correct answers
                    if (animation){
                        cardView.animate().withLayer()
                            .rotationY(90)
                            .setDuration(300)
                            .withEndAction(
                                new Runnable() {
                                    @Override public void run() {
                                        txt_question.setText("Q" + (questionIndex + 1) + ": " +
                                                questions.get(questionIndex).getAnswer());

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
                        txt_question.setText("Q" + (questionIndex + 1) + ": " +
                                questions.get(questionIndex).getAnswer());
                    }

                    // happy face and sad face
                    String answerInput = txt_answer.getText().toString().toLowerCase();
                    String answerCorrect = questions.get(questionIndex).getAnswer().toLowerCase();

                    if(answerCorrect.equals(answerInput)){
                        image_Face.setImageResource(R.drawable.ic_happy_face);
                        faceFlag = 0;
                        totalCorrect++;
                    }
                    else{
                        image_Face.setImageResource(R.drawable.ic_sad_face);
                        faceFlag = 1;
                    }

                    // display the score
                    view_Result.setText("Score: " + totalCorrect + "/" + totalQuestion);


                    // show btn_next
                    if(totalQuestion > (questionIndex + 1)){
                        managerNextButton();
                        //btn_next.setVisibility(View.VISIBLE);
                        //txt_answer.setEnabled(false);
                        //btn_ok.setEnabled(false);
                        nextBtnFlag = true;
                    }
                    else{
                        btn_ok.setText(R.string.play_back_to_home);
                        btn_ok.setOnClickListener(handleClickWhenDone());
                    }

                    break;
                case R.id.btn_next:
                    nextBtnFlag = false;
                    questionIndex++;
                    txt_answer.setText("");
                    btn_next.setVisibility(View.INVISIBLE);
                    btn_ok.setEnabled(true);
                    txt_answer.setEnabled(true);
                    displayQuestion(txt_question, questionIndex);
                    break;
            }
            }
        };
    }

    private void managerNextButton(){
            btn_next.setVisibility(View.VISIBLE);
            txt_answer.setEnabled(false);
            btn_ok.setEnabled(false);
    }

    public View.OnClickListener handleClickWhenDone(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

    @Override
    protected void onPause() {
        Editor editor = sharedPref.edit();
        editor.putInt("questionIndex", questionIndex);
        editor.putBoolean("nextButton", nextBtnFlag);

        editor.putString("questionText", txt_question.getText().toString());

        editor.putInt("faceFlag", faceFlag);
        editor.putString("userAnswer", txt_answer.getText().toString());
        editor.putInt("score", totalCorrect);

        editor.commit();

        Toast.makeText(this, "onPause called", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        questionIndex = sharedPref.getInt("questionIndex", 0);

        nextBtnFlag = sharedPref.getBoolean("nextButton", false);
        if(nextBtnFlag){
            managerNextButton();
        }

        //displayQuestion(txt_question, questionIndex);
        txt_question.setText(sharedPref.getString("questionText",
                "Q" + (questionIndex + 1) + ": " + questions.get(questionIndex).getQuestion()));

        faceFlag = sharedPref.getInt("faceFlag", -1);
        //if(faceFlag != -1){
            reassignFace(faceFlag);
        //}

        txt_answer.setText(sharedPref.getString("userAnswer", ""));

        totalCorrect = sharedPref.getInt("score", 0);
        view_Result.setText("Score: " + totalCorrect + "/" + totalQuestion);

        Toast.makeText(this, "onResume called, questionIndex: " + questionIndex, Toast.LENGTH_SHORT).show();

    }

    private void displayQuestion(TextView txt, int qIndex){
        int questionNo = qIndex + 1;
        txt.setText("Q" + questionNo + ": " + questions.get(qIndex).getQuestion());
        txt_question.requestFocus();
    }

    private void reassignFace(int flag)
    {
        switch (flag)
        {
            case 0:
                image_Face.setImageResource(R.drawable.ic_happy_face);
                break;
            case 1:
                image_Face.setImageResource(R.drawable.ic_sad_face);
                break;
        }
    }

    /*@Override
    protected void onPause() {
        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putInt("questionIndex", questionIndex);
        ed.putInt("collectionId", collectionId);
        ed.putInt("score", totalCorrect);
        ed.putInt("totalQuestion", totalQuestion);
        ed.commit();

        isCreating = false;

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCreating){
            collectionId = sharedPref.getInt("collectionId", 0);
            questionIndex = sharedPref.getInt("questionIndex", 0);
            totalCorrect = sharedPref.getInt("score", 0);
            totalQuestion = sharedPref.getInt("totalQuestion", 0);
            displayQuestion(txt_question, questionIndex);
            view_Result.setText("Score: " + totalCorrect + "/" + totalQuestion);
        }
    }*/
}