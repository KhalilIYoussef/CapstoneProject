package khaliliyoussef.capstoneproject.adapter;//package khaliliyoussef.capstoneproject.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import khaliliyoussef.capstoneproject.R;
import khaliliyoussef.capstoneproject.data.PikContract;
import khaliliyoussef.capstoneproject.model.Post;

import static khaliliyoussef.capstoneproject.data.PikContract.RECIPE_CONTENT_URI;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_DESCRIPTION;
import static khaliliyoussef.capstoneproject.data.PikContract.RecipeEntry.COLUMN_POST_NAME;

/**
 * Created by Khalil on 8/3/2017.
 */
public class PostAdapter extends FirebaseRecyclerAdapter<Post,PostViewHolder>
{
    Post mPost;
    private Context context;
    public PostAdapter(Class<Post> modelClass, int modelLayout, Class<PostViewHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mPost = dataSnapshot.getValue(Post.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void populateViewHolder(final PostViewHolder viewHolder, Post model, int position) {
        viewHolder.postTitle.setText(model.getTitle());
        viewHolder.postDescription.setText(model.getDescription());
        Picasso.with(context).load(model.getPhotoUrl()).into(viewHolder.postImage);
        viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(isFavorite())
            {
                removePostFromFavorites();
                viewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_change);
            }
            else
            {
               addPostToFavorites();
                viewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_change);
            }
            }
        });
    }


    synchronized private boolean isFavorite()
    {

        //get all the recipes where its "id" equal the current recipe if the return cursor is null then it's not fav
        //if the cursor is not null then it's favorite
        //String[] projection = {COLUMN_POST_NAME};
        Cursor cursor = context.getContentResolver().query(RECIPE_CONTENT_URI,
                new String[]{COLUMN_POST_DESCRIPTION},
                COLUMN_POST_NAME + "=?",
                new String[]{mPost.getTitle()},
                null);

//        String selection = COLUMN_POST_NAME + " = ?" ;
//                String [] selectionArgs={mPost.getTitle()} ;
//        Cursor cursor = context.getContentResolver().query(RECIPE_CONTENT_URI,
//                null,
//                selection,
//               selectionArgs,
//                null,
//                null);
        if(cursor!=null)
            Toast.makeText(context, "Post added", Toast.LENGTH_SHORT).show();
        return (cursor != null ? cursor.getCount() : 0) > 0;
    }


    //sync because he might added or remove it  in the activity that we in as many times as he want
    // to completely sync with the database
    synchronized private void removePostFromFavorites() {
        context.getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
    }

    synchronized private void addPostToFavorites()
    {
        //delete the old recipes (it can only save one recipe )
       context. getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
//save every ingredient in the database with the recipe name and id

        ContentValues values = new ContentValues();

        values.put(COLUMN_POST_NAME, mPost.getTitle());
        values.put(PikContract.RecipeEntry.COLUMN_POST_DESCRIPTION, mPost.getDescription());
        values.put(PikContract.RecipeEntry.COLUMN_POST_URL, mPost.getPhotoUrl());

        context.getContentResolver().insert(RECIPE_CONTENT_URI, values);

    }


}
