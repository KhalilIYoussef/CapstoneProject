package khaliliyoussef.capstoneproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import khaliliyoussef.capstoneproject.model.Post;
import khaliliyoussef.capstoneproject.R;

public class PostActivity extends AppCompatActivity {
    private static final int RC_IMG_PICK = 2;
    @BindView(R.id.image_post) ImageButton mPostImage;
    @BindView(R.id.ed_post_title) EditText mPostTitle;
   @BindView(R.id.ed_post_description) EditText mPostDescription;
   @BindView(R.id.bt_submit) Button mSubmitBtn;
    Uri pickedImageUri;
ProgressDialog mProgressDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
            mStorage=FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mProgressDialog=new ProgressDialog(this);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishPost();
            }
        });
        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,RC_IMG_PICK);
            }
        });

    }

    private void publishPost() {
        mProgressDialog.setMessage("Posting ...");
        mProgressDialog.show();
        //notice we used trim() to save space while saving the data
        final String title=mPostTitle.getText().toString().trim();
        final String description=mPostDescription.getText().toString().trim();
        if(!title.isEmpty()&&!description.isEmpty()&&pickedImageUri!=null)
        {
StorageReference filePath =mStorage.child("blog_images").child(pickedImageUri.getLastPathSegment());
            filePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Uri downloadUri=taskSnapshot.getDownloadUrl();
                    mDatabase.push().setValue(new Post(title,description,downloadUri.toString()));
//                    newPost.child("post_title").setValue(title);
//                    newPost.child("post_description").setValue(description);
//                    newPost.child("post_url").setValue(downloadUri);
                    mProgressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_IMG_PICK&&resultCode==RESULT_OK)
        {
          pickedImageUri=data.getData();
            //each time you pick it you display it
            mPostImage.setImageURI(pickedImageUri);
        }
    }
}
