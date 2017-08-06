package khaliliyoussef.capstoneproject.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PikContract {
    static final String CONTENT_AUTHORITY = "khaliliyoussef.capstoneproject.data";
    static final String PATH_POST = "posts";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri RECIPE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POST).build();

    // the first table
    public static final class RecipeEntry implements BaseColumns {
        public static final String COLUMN_POST_NAME = "post_name";
        public static final String COLUMN_POST_DESCRIPTION = "post_desc";
        public static final String COLUMN_POST_URL = "post_url";
        static final String POST_TABLE = "posts";
    }
}
