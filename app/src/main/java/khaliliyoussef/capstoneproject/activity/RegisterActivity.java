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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import khaliliyoussef.capstoneproject.R;
import khaliliyoussef.capstoneproject.model.User;

public class RegisterActivity extends AppCompatActivity
{
@BindView(R.id.ed_register_name) EditText mNameField;
    @BindView(R.id.ed_register_email) EditText mEmailField;
    @BindView(R.id.ed_register_password) EditText mPasswoedField;
    @BindView(R.id.bt_register) Button mSignUp ;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        mProgressDialog=new ProgressDialog(this);
mProgressDialog.setMessage("adding User ...");
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        final String name,email,password;
        name=mNameField.getText().toString();
        email=mEmailField.getText().toString();
        password=mPasswoedField.getText().toString();
        //check if any field is not empty
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
        {
            mProgressDialog.show();
mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
if(task.isSuccessful()) {
    String userId = mFirebaseAuth.getCurrentUser().getUid();

    mDatabaseReference.child(userId).setValue(new User(name, "default"));
    mProgressDialog.dismiss();
    Intent mainIntent =new Intent(RegisterActivity.this,MainActivity.class);
    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(mainIntent);
}
    }
});
        }
    }
}
