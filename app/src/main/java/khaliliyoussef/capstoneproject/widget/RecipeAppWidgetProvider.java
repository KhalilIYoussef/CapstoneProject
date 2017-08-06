package khaliliyoussef.capstoneproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;


import com.squareup.picasso.Picasso;

import khaliliyoussef.capstoneproject.R;
import khaliliyoussef.capstoneproject.activity.MainActivity;
import khaliliyoussef.capstoneproject.data.PikProvider;

import static khaliliyoussef.capstoneproject.data.PikContract.RECIPE_CONTENT_URI;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_DESCRIPTION;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_NAME;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_URL;
import static khaliliyoussef.capstoneproject.widget.RecipeWidgetRemoteViewsService.INDEX_POST_URL;


public class RecipeAppWidgetProvider extends android.appwidget.AppWidgetProvider {

    private static final String[] RECIPE_COLUMNS =
            {

                    COLUMN_POST_NAME,
                    COLUMN_POST_DESCRIPTION,
                    COLUMN_POST_URL,

            };


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them (the user can add as many widget as he like)
        for (int appWidgetId : appWidgetIds) {
            //notice RemoteViews because ordinary views won't work
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pik_club_app_widget);
//           Cursor data = context.getContentResolver().query(RECIPE_CONTENT_URI,
//                    RECIPE_COLUMNS,
//                    null,
//                    null,
//                    null,
//                    null);
//            Picasso.with(context)
//                    .load(String.valueOf(data.getString(INDEX_POST_URL)))
//                    .into(views, R.id.iv_widget_image, new int[] {appWidgetId});
//            //create an Activity upon clicking on the wiget to go to the RecipeActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            //set a click listener when clicking on the widget to go to the RecipeActivity
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            //set the adapter for our widget pass the list items and the
            views.setRemoteAdapter(R.id.lv_posts, new Intent(context, RecipeWidgetRemoteViewsService.class));


            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.lv_posts, clickPendingIntentTemplate);
//            views.setEmptyView(R.id.lv_posts, R.id.widget_empty);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
//if you recived an Intent in which has an ACTION to update the
        if (PikProvider.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_posts);
        }
    }
}

