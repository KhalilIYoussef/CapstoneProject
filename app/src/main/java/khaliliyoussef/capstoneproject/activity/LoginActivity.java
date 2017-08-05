package khaliliyoussef.capstoneproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import khaliliyoussef.capstoneproject.R;

public class LoginActivity extends AppCompatActivity {
@BindView(R.id.email) EditText loginEmail;
    @BindView(R.id.password) EditText loginPasswod;
    @BindView(R.id.email_sign_in_button) Button loginButton;
    @BindView(R.id.new_account) Button loginRegister;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("users");
mProgressDialog =new ProgressDialog(this);
        mProgressDialog.setMessage("checking ..");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck();
            }
        });
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void loginCheck() {

        final String email,password;
        email=loginEmail.getText().toString();
        password=loginPasswod.getText().toString();
        //check if any field is not empty
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
        {
            mProgressDialog.show();
mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
if (task.isSuccessful())
{
    mProgressDialog.dismiss();
    //user has an account exist in (athentication database)
    //chek if he completed completed his profile (exist in Database as User)
checkUser();

}
else {
    mProgressDialog.dismiss();
    Toast.makeText(LoginActivity.this, "error logging in", Toast.LENGTH_SHORT).show();
}
    }
});
        }
    }
/**
 * this method is used to check for the user on the database
 * user which will sign in by using providers(facebook ,G+,Twitter,..)
 * they won't be saved in the database
 * */
    private void checkUser()
    {
final String user_id=mFirebaseAuth.getCurrentUser().getUid();
        //check if the user exist in the database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id))
                {
                    Intent mainIntent =new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else
                    {
                       // Toast.makeText(LoginActivity.this, "Complete Profile", Toast.LENGTH_SHORT).show();
                        Intent mainIntent =new Intent(LoginActivity.this,MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
