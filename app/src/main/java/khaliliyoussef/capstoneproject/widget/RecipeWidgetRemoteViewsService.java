package khaliliyoussef.capstoneproject.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import khaliliyoussef.capstoneproject.R;

import static khaliliyoussef.capstoneproject.data.PikContract.RECIPE_CONTENT_URI;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_URL;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_DESCRIPTION;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_NAME;

/*
* it's like an adapter but only to the widget and instead of onBindViewHolder there is getViewAt
*
* */
// a class that extend RemoteViewsServices must implement onGetViewFactory
public class RecipeWidgetRemoteViewsService extends RemoteViewsService {
    // these indices must match the projection
    static final int INDEX_INDEX_POST_NAME = 0;
    static final int INDEX_POST_DESCRIPTION = 1;
    static final int INDEX_POST_URL = 2;

    private static final String[] RECIPE_COLUMNS =
                             {

                                     COLUMN_POST_NAME,
                                     COLUMN_POST_DESCRIPTION,
                                     COLUMN_POST_URL,

                             };



            //take an Intent as it's parameter
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new RemoteViewsFactory()
        {
            private Cursor data = null;

            @Override
            public void onCreate()
            {

            }

            @Override
            public void onDataSetChanged()
            {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                //get all the recipes from the CP
                data = getContentResolver().query(
                        RECIPE_CONTENT_URI,
                        RECIPE_COLUMNS,
                        null,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy()
            {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == RecyclerView.NO_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.pik_list_item);

                String Title=data.getString(INDEX_INDEX_POST_NAME);
                views.setTextViewText(R.id.tv_widget_title, Title);
                String Description=data.getString(INDEX_POST_DESCRIPTION);
                views.setTextViewText(R.id.tv_widget_description, Description);
//                views.setImageViewResource(R.id.iv_widget_image,R.mipmap.ic_launcher );
                //String.valueOf(data.getString(INDEX_POST_URL))

                views.setOnClickFillInIntent(R.id.post_item, new Intent());
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.pik_list_item);
            }

            @Override
            public int getViewTypeCount()
            {
                return 1;
            }

            @Override
            public long getItemId(int position)
            {
                if (data.moveToPosition(position))
                    return 0;
                return position;
            }

            @Override
            public boolean hasStableIds()
            {
                return true;
            }
        };
    }
}
