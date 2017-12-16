package ph.edu.dlsu.mobidev.machineprojectv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by francis omangayon on 10/26/2017.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "createUser";
    EditText signup_email, signup_password, signup_repassword, signup_username;
    Button signup_button;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signup_email = (EditText) findViewById(R.id.et_signup_email);
        signup_password = (EditText) findViewById(R.id.et_signup_password);
        signup_repassword = (EditText) findViewById(R.id.et_signup_repassword);
        signup_button = (Button) findViewById(R.id.btn_sign_up);
        signup_username = (EditText) findViewById(R.id.et_signup_username);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        signup_button.setOnClickListener(this);
    }

    public void registerUser(){
        String email = signup_email.getText().toString().trim();
        String password = signup_password.getText().toString().trim();
        String confpass = signup_repassword.getText().toString().trim();
        final String username = signup_username.getText().toString().trim();

        if(email.isEmpty()){
            signup_email.setError("Email is required");
            signup_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signup_email.setError("Please enter a valid email");
            signup_email.requestFocus();
            return;
        }
        if(username.isEmpty()){
            signup_username.setError("Username is required");
            signup_username.requestFocus();
            return;
        }
        if(username.length() <= 2 || username.length() >= 17){
            signup_username.setError("Username should be 3-16 characters long");
            signup_username.requestFocus();
            return;
        }
        if(password.isEmpty()){
            signup_password.setError("Password is required");
            signup_password.requestFocus();
            return;
        }
        if(password.length() <= 5){
            signup_password.setError("Password needs to have at least 6 characters");
            signup_password.requestFocus();
            return;
        }
        if(confpass.isEmpty()){
            signup_repassword.setError("Please confirm your password");
            signup_repassword.requestFocus();
            return;
        }
        if(!password.equals(confpass)){
            signup_repassword.setError("Passwords do not match");
            signup_repassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        /*if(!checkUniqueUser(username, email, password)){
            signup_username.setError("Username is already taken");
            signup_username.requestFocus();
            return;
        }*/

        mAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "create user with email: "+task.isSuccessful());
                            progressBar.setVisibility(View.GONE);
                            createNewUser(task.getResult().getUser());
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else{
                            task.getException().getLocalizedMessage();
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
    }

    private void createNewUser(FirebaseUser userFromRegistration) {
        String username = signup_username.getText().toString().trim();
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();

        User user = new User(username, email);

        mDatabase.child("users").child(userId).setValue(user);
        //
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_sign_up:
                registerUser();
                break;
        }
    }
}
