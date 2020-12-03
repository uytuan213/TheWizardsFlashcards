package ca.thewizards.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private boolean darkTheme = false;
    private int fontSize;
    private boolean isCreating = false;

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
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                //TODO start alarm activity
                break;
            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}