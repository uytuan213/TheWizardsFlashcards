package ca.thewizards.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ca.thewizards.flashcards.Model.Collection;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private boolean darkTheme;
    private boolean animation;
    private boolean isCreating = false;
    private ArrayList<Button> buttonList;
    FlashcardsApplication application;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        darkTheme = sharedPref.getBoolean("darkTheme", false);
        animation = sharedPref.getBoolean("animation", false);

        if (darkTheme)
        {
            setTheme(R.style.AppThemeDark);
        } else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonList = new ArrayList<Button>();
        mainLayout = (LinearLayout)findViewById(R.id.main_layout);
        application = ((FlashcardsApplication)getApplication());

        // Add questions for testing
        /*application.addQuestion("This is the second answer", "This is the second question", 1);
        application.addQuestion("This is the third answer", "This is the third question", 1);
        application.addQuestion("This is the fourth answer", "This is the fourth question", 1);*/
    }

    void loadCollections(){
        mainLayout.removeAllViews();
        buttonList.clear();
        List<Collection> colList = application.getCollections();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        params.setMargins(40,5,40,5);
        for (int i = 0; i < colList.size(); i++) {
            Collection col = colList.get(i);
            Button btn = new Button(this);
            btn.setText(col.getName());
            btn.setId(col.getId());
            btn.setOnClickListener(handleCollectionClick());
            buttonList.add(btn);
            mainLayout.addView(btn, params);
        }
    }

    View.OnClickListener handleCollectionClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(getApplicationContext(), PlayActivity.class);
                playIntent.putExtra("darkTheme", darkTheme);
                playIntent.putExtra("animation", animation);
                playIntent.putExtra("collectionId", v.getId());
                startActivity(playIntent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean newThemeValue = sharedPref.getBoolean("darkTheme", false);
        if (newThemeValue != darkTheme){
            darkTheme = newThemeValue;
            finish();
            startActivity(getIntent());
        }

        // Generate collection buttons from database
        loadCollections();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flashcards_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("darkTheme", darkTheme);
                startActivity(intent);

                break;
            case R.id.alarm:
                Intent alarmIntent = new Intent(getApplicationContext(), AlarmActivity.class);
                alarmIntent.putExtra("darkTheme", darkTheme);
                startActivity(alarmIntent);
                break;
            case R.id.manage_collections:
                Intent manageCollectionIntent = new Intent(getApplicationContext(), ManageCollectionsActivity.class);
                manageCollectionIntent.putExtra("darkTheme", darkTheme);
                startActivity(manageCollectionIntent);
                break;
            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}