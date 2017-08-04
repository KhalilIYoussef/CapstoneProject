package khaliliyoussef.capstoneproject.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import khaliliyoussef.capstoneproject.R;
import khaliliyoussef.capstoneproject.adapter.PostAdapter;
import khaliliyoussef.capstoneproject.adapter.PostViewHolder;
import khaliliyoussef.capstoneproject.model.Post;

public class MainActivity extends AppCompatActivity {
@BindView(R.id.rv_posts_list)RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
   @BindView(R.id.addPost_fab) FloatingActionButton fab;
    private DatabaseReference mDatabase;
private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth=FirebaseAuth.getInstance();

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
Intent loginIntent =new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);

        PostAdapter postAdapter=new PostAdapter(Post.class,R.layout.post_list_item,PostViewHolder.class,mDatabase,this);
        mRecyclerView.setAdapter(postAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if (item.getItemId()==R.id.sign_out_menu)
       {
            signOut();

       }
return  super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mAuth.signOut();
    }
}
