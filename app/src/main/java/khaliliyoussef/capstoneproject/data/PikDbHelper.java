package khaliliyoussef.capstoneproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import khaliliyoussef.capstoneproject.data.PikContract.*;

import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.*;


public final class PikDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "post.db";

    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_POST_TABLE =
            "CREATE TABLE " + POST_TABLE + " (" +
             _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_POST_NAME + " TEXT NOT NULL, " +
            COLUMN_POST_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMN_POST_URL + " TEXT NOT NULL " +

            ");";

    public PikDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("ALTER TABLE IF EXISTS " + POST_TABLE);
        onCreate(db);
    }
}
