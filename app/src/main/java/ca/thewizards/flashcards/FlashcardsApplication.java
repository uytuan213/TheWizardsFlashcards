package ca.thewizards.flashcards;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ca.thewizards.flashcards.Model.Collection;
import ca.thewizards.flashcards.Model.Question;

public class FlashcardsApplication extends Application {
    private static final String DB_NAME = "db_flashcards";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_COLLECTIONS = "tbl_collections";
    private static final String TABLE_NAME_QUESTIONS = "tbl_questions";
    private SQLiteOpenHelper helper;

    @Override
    public void onCreate() {
        helper = new SQLiteOpenHelper(this, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_COLLECTIONS + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL)");

                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_QUESTIONS + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " answer TEXT NOT NULL," +
                        " question TEXT NOT NULL," +
                        " collectionId INTEGER )");
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                // No-op
            }
        };

        super.onCreate();
    }

    public Collection getCollection(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_COLLECTIONS + " WHERE id = " + id, null);

        cursor.moveToFirst();

        String name = cursor.getString(1);
        Collection collection = new Collection(id, name);

        cursor.close();

        return(collection);
    }

    public ArrayList<Collection> getCollections(){
        ArrayList<Collection> list = new ArrayList<Collection>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_COLLECTIONS, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            do {
                int colId = cursor.getInt(0);
                String colName = cursor.getString(1);
                Collection col = new Collection(colId, colName);
                list.add(col);
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public void addCollection(String name) {
        try{
            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL("INSERT INTO " + TABLE_NAME_COLLECTIONS + "(name) "
                    + "VALUES ('" + name + "')");
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
        }

    }

    public void renameCollection(int id, String name) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME_COLLECTIONS + " SET name = '" + name + "' WHERE id = " + id + ")");
    }

    public void deleteCollection(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        // Delete the Collection
        db.execSQL("DELETE FROM " + TABLE_NAME_COLLECTIONS + "WHERE id = " + id + ")");

        // Delete All the Questions in the Collection
        db.execSQL("DELETE FROM " + TABLE_NAME_QUESTIONS + "WHERE collectionId = " + id + ")");
    }

    public void addQuestion(String answer, String question, int collectionId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("INSERT INTO " + TABLE_NAME_QUESTIONS + "(answer, question, collectionId) "
                + "VALUES ('" + answer + "', '" + question + "', " + collectionId + ")");
    }

    public ArrayList<Question> getQuestions(int collectionId){
        ArrayList<Question> list = new ArrayList<Question>();
        SQLiteDatabase db = helper.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_QUESTIONS + " WHERE collectionId=" + collectionId, null);

            cursor.moveToFirst();
            if (cursor.getCount() > 0){
                do {
                    int questionId = cursor.getInt(0);
                    String answer = cursor.getString(1);
                    String question = cursor.getString(2);
                    Question ques = new Question(questionId, answer, question, collectionId);
                    list.add(ques);
                }
                while(cursor.moveToNext());
            }

            cursor.close();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
        }
        return list;

    }

    public void deleteQuestion(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME_QUESTIONS + "WHERE id = " + id + ")");
    }
}
