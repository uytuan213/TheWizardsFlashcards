package ca.thewizards.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ca.thewizards.flashcards.Model.Collection;

public class ManageCollectionsActivity extends AppCompatActivity {

    private boolean darkTheme;
    ArrayList<Collection> colList;
    LinearLayout linearLayout;
    LinearLayout addColLayout;
    ScrollView scrollView;
    FlashcardsApplication application;
    FloatingActionButton fab;
    Button addCollectionName;
    Button backToCollection;
    TextInputEditText txtCollectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        darkTheme = intent.getBooleanExtra("darkTheme", false);

        if (darkTheme){
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_collections);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        application = (FlashcardsApplication)getApplication();

        addColLayout = (LinearLayout)  findViewById(R.id.add_col_linear_layout);
        linearLayout = (LinearLayout) findViewById(R.id.manage_col_linear_layout);
        scrollView = findViewById(R.id.scroll_view);
        fab = findViewById(R.id.manage_col_fab);
        addCollectionName = findViewById(R.id.btn_add);
        backToCollection = findViewById(R.id.btn_back_to_collections);
        txtCollectionName = findViewById(R.id.txt_collection_name);

        //Auto-generate collection buttons
        LoadCollections();

        // Add fab listener
        fab.setOnClickListener(handleClick());

        // Add addCollectionName listener
        addCollectionName.setOnClickListener(handleClick());

        //Add backToCollection listener
        backToCollection.setOnClickListener(handleClick());
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
                if (v.getId() == R.id.manage_col_fab){
                    scrollView.setVisibility(View.INVISIBLE);
                    addColLayout.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    txtCollectionName.setText("");
                } else{
                    if (v.getId() == R.id.btn_add) {
                        // add new collection to db
                        String newCollection = txtCollectionName.getText().toString();
                        application.addCollection(newCollection);
                        Snackbar.make(findViewById(R.id.manage_col_main_layout), R.string.collection_added, Snackbar.LENGTH_LONG).show();
                    }

                    LoadCollections();
                    scrollView.setVisibility(View.VISIBLE);
                    addColLayout.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }

            }
        };
    }

    void LoadCollections(){
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        params.setMargins(40,5,40,5);
        colList = application.getCollections();
        for (int i = 0; i < colList.size(); i++) {
            Collection col = colList.get(i);
            Button btn = new Button(this);
            btn.setText(col.getName());
            btn.setId(col.getId());
            btn.setOnClickListener(handleCollectionClick());
            // TODO Add btn to buttonList
            linearLayout.addView(btn, params);
        }
    }

    View.OnClickListener handleCollectionClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageQuestionsIntent = new Intent(getApplicationContext(), ManageQuestionsActivity.class);
                manageQuestionsIntent.putExtra("darkTheme", darkTheme);
                manageQuestionsIntent.putExtra("collectionId", v.getId());
                startActivity(manageQuestionsIntent);
            }
        };
    }
}