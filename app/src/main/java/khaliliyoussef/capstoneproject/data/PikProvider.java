package khaliliyoussef.capstoneproject.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static khaliliyoussef.capstoneproject.data.PikContract.CONTENT_AUTHORITY;
import static khaliliyoussef.capstoneproject.data.PikContract.PATH_POST;
import static khaliliyoussef.capstoneproject.data.PikContract.RECIPE_CONTENT_URI;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.POST_TABLE;


public class PikProvider extends ContentProvider {
    private PikDbHelper mOpenHelper;
    public static final int CODE_RECIPE = 100;
    public static final int CODE_RECIPE_WITH_ID = 101;
    //if any Update happen to the Database use this string to identify the broadcast
    public static final String ACTION_DATA_UPDATED = "khaliliyoussef.capstoneproject.data.ACTION_DATA_UPDATED";

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_POST, CODE_RECIPE);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_POST + "/#", CODE_RECIPE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PikDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_WITH_ID:
                cursor = db.query(POST_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_RECIPE:
                cursor = db.query(POST_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long id;

        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_WITH_ID:
                id = db.insert(POST_TABLE, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RECIPE_CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case CODE_RECIPE:
                id = db.insertWithOnConflict(POST_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RECIPE_CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        //TODO when data changed then send a broadcast then use that to update the widget_provider later
        //Update the Widget with the data
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(getContext().getPackageName());
        getContext().sendBroadcast(dataUpdatedIntent);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numberOfDeletedIngredients;
        if (null == selection)
            selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_WITH_ID:
                numberOfDeletedIngredients = db.delete(POST_TABLE,
                        selection,
                        selectionArgs);
                break;
            case CODE_RECIPE:
                numberOfDeletedIngredients = db.delete(POST_TABLE, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);

        }

        if (numberOfDeletedIngredients != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            //Update The Widget and send a broadcast that the data has changed
            // Setting the package ensures that only components in our app will receive the broadcast
            Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                    .setPackage(getContext().getPackageName());
            getContext().sendBroadcast(dataUpdatedIntent);

        }
        return numberOfDeletedIngredients;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
