package khaliliyoussef.capstoneproject.adapter;//package khaliliyoussef.capstoneproject.adapter;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import khaliliyoussef.capstoneproject.model.Post;

/**
 * Created by Khalil on 8/3/2017.
 */
public class PostAdapter extends FirebaseRecyclerAdapter<Post,PostViewHolder>
{
    private Context context;
    public PostAdapter(Class<Post> modelClass, int modelLayout, Class<PostViewHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
        viewHolder.postTitle.setText(model.getTitle());
        viewHolder.postDescription.setText(model.getDescription());
        Glide.with(context).load(model.getPhotoUrl()).into(viewHolder.postImage);
    }



}
