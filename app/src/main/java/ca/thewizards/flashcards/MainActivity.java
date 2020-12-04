package ca.thewizards.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private boolean darkTheme = false;
    private int fontSize;
    private boolean isCreating = false;
    private List<Button> buttonList;

    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        darkTheme = sharedPref.getBoolean("darkTheme", false);
        fontSize = sharedPref.getInt("fontSize", 24);

        if (darkTheme)
        {
            setTheme(R.style.AppThemeDark);
        } else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate collection buttons from database
        // TODO
        /*int countCollection = [get collection count from db]
        buttonList = new Button[countCollection];
        */
        mainLayout = (LinearLayout)findViewById(R.id.main_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        params.setMargins(40,5,40,5);
        for (int i = 0; i < 5; i++) {
            Button btn = new Button(this);
            btn.setText("Button #" + i);
            btn.setId(i);
            // TODO Add btn to buttonList
            mainLayout.addView(btn, params);
        }
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
            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}