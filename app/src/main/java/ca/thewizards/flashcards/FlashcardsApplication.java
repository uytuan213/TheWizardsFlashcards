package ca.thewizards.flashcards;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ca.thewizards.flashcards.Model.Collection;
import ca.thewizards.flashcards.Model.Question;

public class FlashcardsApplication extends Application {
    private static final String DB_NAME = "db_tax_stats";
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
                        "cost_paid REAL, tax_paid REAL)");
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

    public void addCollection(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("INSERT INTO " + TABLE_NAME_COLLECTIONS + "(name) "
                + "VALUES (" + name + ")");
    }

    public void renameCollection(int id, String name) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME_COLLECTIONS + " SET name = " + name + " WHERE id = " + id + ")");
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
                + "VALUES (" + answer + ", " + question + ", " + collectionId + ")");
    }

    public void deleteQuestion(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME_QUESTIONS + "WHERE id = " + id + ")");
    }
}
