package khaliliyoussef.capstoneproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import khaliliyoussef.capstoneproject.R;

/**
 * Created by Khalil on 8/3/2017.
 */


public class PostViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_post_title)
    TextView postTitle;
    @BindView(R.id.list_post_image)
    ImageView postImage;
    @BindView(R.id.list_post_description)
    TextView postDescription;
    @BindView(R.id.ic_favorite)
    ImageView ivFavorite;

    public PostViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

