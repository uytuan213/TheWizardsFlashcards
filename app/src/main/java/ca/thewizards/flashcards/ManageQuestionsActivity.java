package ca.thewizards.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ca.thewizards.flashcards.Model.Question;

public class ManageQuestionsActivity extends AppCompatActivity {

    private boolean darkTheme;
    int collectionId;
    ArrayList<Question> quesList;
    LinearLayout linearLayout;
    LinearLayout addQuesLayout;
    ScrollView scrollView;
    FlashcardsApplication application;
    FloatingActionButton fab;
    Button addQuestion;
    Button backToQuestions;
    TextInputEditText txtQuestion;
    TextInputEditText txtQuestionAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        darkTheme = intent.getBooleanExtra("darkTheme", false);

        if (darkTheme){
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }

        // Load the selected collection id from intent
        collectionId = intent.getIntExtra("collectionId", 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        application = (FlashcardsApplication)getApplication();

        addQuesLayout = (LinearLayout)  findViewById(R.id.add_ques_linear_layout);
        linearLayout = (LinearLayout) findViewById(R.id.manage_ques_linear_layout);
        scrollView = findViewById(R.id.scroll_view);
        fab = findViewById(R.id.manage_ques_fab);
        addQuestion = findViewById(R.id.btn_add);
        backToQuestions = findViewById(R.id.btn_back_to_questions);
        txtQuestion = findViewById(R.id.txt_question);
        txtQuestionAnswer = findViewById(R.id.txt_question_answer);

        //Auto-generate question buttons
        LoadQuestions();

        // Add fab listener
        fab.setOnClickListener(handleClick());

        // Add addQuestion listener
        addQuestion.setOnClickListener(handleClick());

        //Add backToQuestions listener
        backToQuestions.setOnClickListener(handleClick());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    View.OnClickListener handleClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.manage_ques_fab){
                    scrollView.setVisibility(View.INVISIBLE);
                    addQuesLayout.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    txtQuestion.setText("");
                    txtQuestionAnswer.setText("");
                } else{
                    if (v.getId() == R.id.btn_add) {
                        // add new question to db
                        String newQuestion = txtQuestion.getText().toString();
                        String newQuestionAnswer = txtQuestionAnswer.getText().toString();
                        application.addQuestion(newQuestionAnswer, newQuestion, collectionId);
                        Snackbar.make(findViewById(R.id.manage_ques_main_layout), R.string.question_added, Snackbar.LENGTH_LONG).show();
                    }

                    LoadQuestions();
                    scrollView.setVisibility(View.VISIBLE);
                    addQuesLayout.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }

            }
        };
    }

    void LoadQuestions(){
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        params.setMargins(40,5,40,5);
        quesList = application.getQuestions(collectionId);
        for (int i = 0; i < quesList.size(); i++) {
            Question ques = quesList.get(i);
            Button btn = new Button(this);
            btn.setText(ques.getAnswer());
            btn.setId(ques.getId());
            btn.setOnClickListener(handleQuestionClick());
            // TODO Add btn to buttonList
            linearLayout.addView(btn, params);
        }
    }

    View.OnClickListener handleQuestionClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editQuestionIntent = new Intent(getApplicationContext(), EditQuestionActivity.class);
                editQuestionIntent.putExtra("darkTheme", darkTheme);
                editQuestionIntent.putExtra("questionId", v.getId());
                startActivity(editQuestionIntent);
            }
        };
    }
}